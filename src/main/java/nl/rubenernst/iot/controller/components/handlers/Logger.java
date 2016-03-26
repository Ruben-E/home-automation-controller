package nl.rubenernst.iot.controller.components.handlers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.gateway.GatewayObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Logger {
    @Autowired
    public Logger(GatewayObservable gateway) {
        gateway.getObservable()
                .subscribe(
                        pair -> {
                            log.debug("Received data: " + pair);
                        },
                        e -> {
                            log.warn("Got exception.", e);
                        });

    }
}
