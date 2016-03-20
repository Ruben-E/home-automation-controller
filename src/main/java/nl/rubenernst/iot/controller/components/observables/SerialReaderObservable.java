package nl.rubenernst.iot.controller.components.observables;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.builder.MessageBuilder;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SerialReaderObservable {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    private static final String PORT_NAMES[] = {
            "/dev/cu", // Mac OS X
            "/dev/usbdev", // Linux
            "/dev/tty", // Linux
            "/dev/serial", // Linux
            "COM3", // Windows
    };

    @Autowired
    private MessageBuilder messageBuilder;

    Observable<Pair<Message, OutputStream>> sharedObservable;

    public Observable<Pair<Message, OutputStream>> getSerialPortObservable() {
        if (sharedObservable == null) {
            Observable<Pair<Message, OutputStream>> observable = Observable.create(subscriber -> {
                try {
                    CommPortIdentifier portIdentifier = null;
                    SerialPort serialPort = null;
                    Enumeration ports = CommPortIdentifier.getPortIdentifiers();

                    System.out.println("Trying:");
                    while (portIdentifier == null && ports.hasMoreElements()) {
                        CommPortIdentifier currentPortId = (CommPortIdentifier) ports.nextElement();
                        if (currentPortId.getName().contains("Bluetooth")) {
                            continue;
                        }

                        System.out.println("   port" + currentPortId.getName());
                        for (String portName : PORT_NAMES) {
                            if (currentPortId.getName().equals(portName) || currentPortId.getName().startsWith(portName)) {
                                serialPort = (SerialPort) currentPortId.open("SerialReader", 2000);
                                portIdentifier = currentPortId;
                                System.out.println("Connected on port" + currentPortId.getName());

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
                                    EXECUTOR_SERVICE.submit(() -> {
                                        List<Message> messages = messageBuilder.fromPayload(payload);
                                        for (Message message : messages) {
                                            subscriber.onNext(new Pair<>(message, output));
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                EXECUTOR_SERVICE.submit(() -> subscriber.onError(e));
                            }
                        });
                        serialPort.notifyOnDataAvailable(true);
                    }
                } catch (Exception e) {
                    EXECUTOR_SERVICE.submit(() -> subscriber.onError(e));
                }
            });
            sharedObservable = observable.share();
        }

        return sharedObservable;
    }
}
