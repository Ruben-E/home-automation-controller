package nl.rubenernst.iot.controller.components.handlers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.gateway.GatewayObservable;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.domain.messages.PresentationMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegisterNodeHandler {
    @Autowired
    public RegisterNodeHandler(GatewayObservable gateway, NodeManager nodeManager) {
        gateway.getObservable()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.PRESENTATION &&
                            message.getMessageSubType() == PresentationMessageSubType.S_ARDUINO_NODE &&
                            message.getSensorId() == 255;
                })
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setNodeType(nodeId, NodeType.NODE);
                });
    }
}
