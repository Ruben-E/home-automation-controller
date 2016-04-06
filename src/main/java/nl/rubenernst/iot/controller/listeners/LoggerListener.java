package nl.rubenernst.iot.controller.listeners;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.gateways.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggerListener {
    @Autowired
    public LoggerListener(Gateway gateway, ExceptionHandler exceptionHandler) {
        gateway.getGateway()
                .subscribe(
                        pair -> {
                            log.debug("Received data: " + pair);
                        },
                        exceptionHandler);

    }
}
