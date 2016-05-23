package nl.rubenernst.iot.controller.actors;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.filters.NodeVersionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeVersionUpdateActor {
    @Autowired
    public NodeVersionUpdateActor(NodeVersionFilter nodeVersionFilter, NodeManager nodeManager, ExceptionHandler exceptionHandler) {
        nodeVersionFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();

                    nodeManager.setVersion(nodeId, message.getPayload());
                }, exceptionHandler);
    }
}
