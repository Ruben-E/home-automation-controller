package nl.rubenernst.iot.controller.gateways;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.domain.messages.SetReqMessageSubType;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@ConditionalOnExpression("'${gateway.type}'=='debug'")
public class DebugGateway implements Gateway {
    @Getter
    private Observable<Pair<Message, OutputStream>> gateway;

    @Autowired
    public DebugGateway(ExecutorService executorService) {
        Observable<Pair<Message, OutputStream>> observable = Observable.create(subscriber -> {
            try {
                while (true) {
                    subscriber.onNext(new Pair<Message, OutputStream>(new Message(1, 1, MessageType.SET, SetReqMessageSubType.V_TEMP, 0, "10.0"), new OutputStream() {
                        @Override
                        public void write(int b) throws IOException {

                        }
                    }));

                    Thread.sleep(10);
                }

            } catch (Exception e) {
                executorService.submit(() -> subscriber.onError(e));
            }
        });
        this.gateway = observable
                .share()
                .doOnError(throwable -> {
                    log.error("Got exception", throwable);
                })
                .subscribeOn(Schedulers.from(executorService));
    }
}
