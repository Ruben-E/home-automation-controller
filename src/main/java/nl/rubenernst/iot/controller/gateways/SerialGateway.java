package nl.rubenernst.iot.controller.gateways;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.builder.MessageBuilder;
import nl.rubenernst.iot.controller.exceptions.NoPortAvailableException;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@ConditionalOnExpression("'${gateway.type}'=='serial'")
public class SerialGateway implements Gateway {
    private static final String PORT_NAMES[] = {
            "/dev/cu", // Mac OS X
            "/dev/usbdev", // Linux
            "/dev/tty", // Linux
            "/dev/serial", // Linux
            "COM3", // Windows
    };

    @Getter
    private Observable<Pair<Message, OutputStream>> gateway;

    @Autowired
    public SerialGateway(MessageBuilder messageBuilder, ExecutorService executorService, ExceptionHandler exceptionHandler) {
        Observable<Pair<Message, OutputStream>> observable = Observable.create(subscriber -> {
            try {
                CommPortIdentifier portIdentifier = null;
                SerialPort serialPort = null;
                Enumeration ports = CommPortIdentifier.getPortIdentifiers();

                log.info("Trying:");
                while (portIdentifier == null && ports.hasMoreElements()) {
                    CommPortIdentifier currentPortId = (CommPortIdentifier) ports.nextElement();
                    if (currentPortId.getName().contains("Bluetooth")) {
                        continue;
                    }

                    log.info("   port" + currentPortId.getName());
                    for (String portName : PORT_NAMES) {
                        if (currentPortId.getName().equals(portName) || currentPortId.getName().startsWith(portName)) {
                            serialPort = (SerialPort) currentPortId.open("SerialReader", 2000);
                            portIdentifier = currentPortId;
                            log.info("Connected on port" + currentPortId.getName());

                            break;
                        }
                    }
                }

                if (serialPort != null) {
                    serialPort.setSerialPortParams(115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

                    BufferedReader input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                    OutputStream output = serialPort.getOutputStream();
                    serialPort.addEventListener(serialPortEvent -> {
                        try {
                            if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE && input.ready()) {
                                String payload = input.readLine();
                                List<Message> messages = messageBuilder.fromPayload(payload);
                                for (Message message : messages) {
                                    subscriber.onNext(new Pair<>(message, output));
                                }
                            }
                        } catch (Exception e) {
                            exceptionHandler.call(e);
                        }
                    });
                    serialPort.notifyOnDataAvailable(true);
                } else {
                    throw new NoPortAvailableException("There is no port available");
                }
            } catch (Exception e) {
                exceptionHandler.call(e);
            }
        });
        this.gateway = observable
                .share()
                .subscribeOn(Schedulers.from(executorService));
    }
}
