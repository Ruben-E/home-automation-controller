package nl.rubenernst.iot.controller.consumers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.message_filters.MessageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeNameConsumer {
    @Autowired
    public NodeNameConsumer(MessageFilter nodeNameMessageFilter, NodeManager nodeManager) {
        nodeNameMessageFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setName(nodeId, message.getPayload());
                });
    }
}
