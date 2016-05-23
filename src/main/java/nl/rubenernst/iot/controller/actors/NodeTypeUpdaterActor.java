package nl.rubenernst.iot.controller.actors;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.NodeType;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.filters.NodeTypeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeTypeUpdaterActor {
    @Autowired
    public NodeTypeUpdaterActor(NodeTypeFilter nodeTypeFilter, NodeManager nodeManager, ExceptionHandler exceptionHandler) {
        nodeTypeFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setNodeType(nodeId, NodeType.NODE);
                }, exceptionHandler);
    }
}
