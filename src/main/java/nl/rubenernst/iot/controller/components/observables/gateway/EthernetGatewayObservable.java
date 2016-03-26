package nl.rubenernst.iot.controller.components.observables.gateway;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.builder.MessageBuilder;
import org.javatuples.Pair;
import rx.Observable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class EthernetGatewayObservable implements GatewayObservable {
    private static final ExecutorService SOCKET_LISTENER = Executors.newSingleThreadExecutor();

    @Getter
    private Observable<Pair<Message, OutputStream>> observable;

    public EthernetGatewayObservable(MessageBuilder messageBuilder, ExecutorService executorService, String gatewayIp, int gatewayPort) {
        Observable<Pair<Message, OutputStream>> observable = Observable.create(subscriber -> {
            SOCKET_LISTENER.submit(() -> {
                try {
                    log.info("Connecting to [{}:{}]", gatewayIp, gatewayPort);

                    Socket socket = new Socket();
                    socket.setKeepAlive(true);
                    socket.connect(new InetSocketAddress(gatewayIp, gatewayPort), 5000);
                    if (socket.isConnected()) {
                        log.info("Connected to ethernet gateway on [{}:{}]", gatewayIp, gatewayPort);

                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = socket.getInputStream();

                        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
                        while (socket.isConnected()) {
                            String payload = input.readLine();

                            executorService.submit(() -> {
                                List<Message> messages = messageBuilder.fromPayload(payload);
                                for (Message message : messages) {
                                    subscriber.onNext(new Pair<>(message, outputStream));
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    executorService.submit(() -> subscriber.onError(e));
                }
            });
        });
        this.observable = observable.share()
                .doOnError(throwable -> {
                    log.error("Got exception", throwable);
                });
    }
}
