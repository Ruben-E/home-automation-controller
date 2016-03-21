package nl.rubenernst.iot.controller.components.subscribers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.SerialReaderObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Logger {
    @Autowired
    public Logger(SerialReaderObservable serialReaderObservable) {
        serialReaderObservable.getObservable()
                .subscribe(
                        System.out::println,
                        e -> {
                            log.warn("Got exception.", e);
                        });

    }
}
