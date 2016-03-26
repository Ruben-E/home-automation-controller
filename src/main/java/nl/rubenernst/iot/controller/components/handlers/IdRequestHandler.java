package nl.rubenernst.iot.controller.components.handlers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ResponseHandler;
import nl.rubenernst.iot.controller.components.observables.gateway.GatewayObservable;
import nl.rubenernst.iot.controller.domain.messages.InternalMessage;
import nl.rubenernst.iot.controller.domain.messages.InternalMessageSubType;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.data.NodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
@Slf4j
public class IdRequestHandler {
    @Autowired
    public IdRequestHandler(GatewayObservable gateway, ResponseHandler responseHandler, NodeManager nodeManager) {
        gateway.getObservable()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.INTERNAL && message.getMessageSubType() == InternalMessageSubType.I_ID_REQUEST;
                })
                .subscribe(pair -> {
                    OutputStream outputStream = pair.getValue1();
                    Message message = pair.getValue0();

                    Node node = nodeManager.createNode();

                    InternalMessage responseMessage = new InternalMessage(message.getNodeId(), message.getSensorId(), InternalMessageSubType.I_ID_RESPONSE, 0, String.valueOf(node.getId()));
                    responseHandler.sendResponse(responseMessage, outputStream);
                });
    }
}
