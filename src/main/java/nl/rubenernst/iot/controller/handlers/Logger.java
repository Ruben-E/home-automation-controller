package nl.rubenernst.iot.controller.handlers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.gateways.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Logger {
    @Autowired
    public Logger(Gateway gateway) {
        gateway.getGateway()
                .subscribe(
                        pair -> {
                            log.debug("Received data: " + pair);
                        },
                        e -> {
                            log.warn("Got exception.", e);
                        });

    }
}
