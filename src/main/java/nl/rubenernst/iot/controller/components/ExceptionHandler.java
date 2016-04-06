package nl.rubenernst.iot.controller.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import rx.functions.Action1;

@Slf4j
@Component
public class ExceptionHandler implements Action1<Throwable> {
    @Override
    public void call(Throwable throwable) {
        log.error("Got exception", throwable);
    }
}
