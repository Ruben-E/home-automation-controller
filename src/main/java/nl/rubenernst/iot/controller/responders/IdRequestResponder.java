package nl.rubenernst.iot.controller.responders;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ResponseSender;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.Node;
import nl.rubenernst.iot.controller.domain.mysensors.InternalMessage;
import nl.rubenernst.iot.controller.domain.mysensors.InternalMessageSubType;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.filters.IdRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
@Slf4j
public class IdRequestResponder {
    @Autowired
    public IdRequestResponder(IdRequestFilter idRequestFilter, ResponseSender responseSender, NodeManager nodeManager) {
        idRequestFilter.getMessages()
                .subscribe(pair -> {
                    OutputStream outputStream = pair.getValue1();
                    Message message = pair.getValue0();

                    Node node = nodeManager.createNode();

                    InternalMessage responseMessage = new InternalMessage(message.getNodeId(), message.getSensorId(), InternalMessageSubType.I_ID_RESPONSE, 0, String.valueOf(node.getId()));
                    responseSender.sendResponse(responseMessage, outputStream);
                });
    }
}
