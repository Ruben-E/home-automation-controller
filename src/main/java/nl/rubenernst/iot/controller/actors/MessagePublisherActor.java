package nl.rubenernst.iot.controller.actors;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.converters.NodeMessageConverter;
import nl.rubenernst.iot.controller.domain.NodeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessagePublisherActor {
    @Autowired
    public MessagePublisherActor(NodeMessageConverter nodeMessageConverter, ExceptionHandler exceptionHandler) {
        nodeMessageConverter.getMessages()
                .subscribe(pair -> {
                    NodeMessage nodeMessage = pair.getValue0();

                }, exceptionHandler);
    }
}
