package nl.rubenernst.iot.controller.listeners;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.message_filters.MessageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeVersionListener {
    @Autowired
    public NodeVersionListener(MessageFilter nodeVersionMessageFilter, NodeManager nodeManager) {
        nodeVersionMessageFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setVersion(nodeId, message.getPayload());
                });
    }
}
