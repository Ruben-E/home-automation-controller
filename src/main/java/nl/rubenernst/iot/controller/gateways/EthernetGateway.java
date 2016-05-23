package nl.rubenernst.iot.controller.gateways;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.domain.mysensors.builder.MessageBuilder;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@ConditionalOnExpression("'${gateway.type}'=='ethernet'")
public class EthernetGateway implements Gateway {
    private boolean stopped = false;
    private Socket socket = new Socket();

    @Getter
    private Observable<Pair<Message, OutputStream>> gateway;

    @Autowired
    public EthernetGateway(MessageBuilder messageBuilder, ExecutorService executorService, ExceptionHandler exceptionHandler, @Value("${gateway.ip}") String gatewayIp, @Value("${gateway.port}") int gatewayPort) {
        Observable<Pair<Message, OutputStream>> observable = Observable.create(subscriber -> {
            try {
                OutputStream outputStream = null;
                BufferedReader input = null;

                socket = new Socket();
                socket.setKeepAlive(true);
                socket.setSoTimeout(1000);

                while (!stopped) {
                    if (!socket.isConnected()) {
                        log.info("Disconnected from ethernet gateway on [{}:{}]", gatewayIp, gatewayPort);

                        try {
                            connectSocketTo(gatewayIp, gatewayPort);

                            outputStream = socket.getOutputStream();
                            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        } catch (Exception e) {
                            log.info("Unable to connect to the gateway on [{}:{}]. Retrying in 1000ms.", gatewayIp, gatewayPort);
                            Thread.sleep(1000);
                            continue;
                        }
                    }

                    if (socket.isConnected() && outputStream != null && input != null) {
                        try {
                            String payload = input.readLine();

                            List<Message> messages = messageBuilder.fromPayload(payload);
                            for (Message message : messages) {
                                subscriber.onNext(new Pair<>(message, outputStream));
                            }
                        } catch (SocketTimeoutException e) {
                            log.trace("Didn't get data within 1000ms.");
                        } catch (Exception e) {
                            log.debug("Got exception. Continuing");
                        }
                    }
                }
            } catch (Exception e) {
                exceptionHandler.call(e);
            }
        });
        this.gateway = observable
                .share()
                .subscribeOn(Schedulers.from(executorService));
    }

    private void connectSocketTo(String gatewayIp, int gatewayPort) throws IOException {
        log.info("Connecting to ethernet gateway on [{}:{}]", gatewayIp, gatewayPort);
        socket.connect(new InetSocketAddress(gatewayIp, gatewayPort), 5000);
        log.info("Connected to ethernet gateway on [{}:{}]", gatewayIp, gatewayPort);
    }

    @PreDestroy
    public void stop() throws IOException {
        stopped = true;
        socket.close();
    }
}
