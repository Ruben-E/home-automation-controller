package nl.rubenernst.iot.controller.actors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.iot.service.sdk.Message;
import com.microsoft.azure.iot.service.sdk.ServiceClient;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.converters.NodeMessageConverter;
import nl.rubenernst.iot.controller.domain.Node;
import nl.rubenernst.iot.controller.domain.NodeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class MessagePublisherActor {
    @Autowired
    public MessagePublisherActor(NodeMessageConverter nodeMessageConverter, ServiceClient serviceClient, ObjectMapper objectMapper, ExceptionHandler exceptionHandler) {
        nodeMessageConverter.getMessages()
                .subscribe(pair -> {
                    try {
                        NodeMessage nodeMessage = pair.getValue0();
                        String json = objectMapper.writeValueAsString(nodeMessage);

                        String deviceId = null;
                        Node node = nodeMessage.getNode();
                        if (node != null) {
                            deviceId = String.valueOf(node.getId());
                        }
                        serviceClient.sendAsync(deviceId, new Message(json));
                    } catch (JsonProcessingException | UnsupportedEncodingException e) {
                        exceptionHandler.call(e);
                    }
                }, exceptionHandler);
    }
}