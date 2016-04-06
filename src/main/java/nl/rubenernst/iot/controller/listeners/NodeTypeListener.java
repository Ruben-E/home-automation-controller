package nl.rubenernst.iot.controller.listeners;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.nodes.NodeType;
import nl.rubenernst.iot.controller.message_filters.MessageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeTypeListener {
    @Autowired
    public NodeTypeListener(MessageFilter nodeTypeMessageFilter, NodeManager nodeManager, ExceptionHandler exceptionHandler) {
        nodeTypeMessageFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setNodeType(nodeId, NodeType.NODE);
                }, exceptionHandler);
    }
}
