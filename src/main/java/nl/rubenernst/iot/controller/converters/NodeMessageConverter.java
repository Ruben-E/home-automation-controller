package nl.rubenernst.iot.controller.converters;

import lombok.Getter;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.Node;
import nl.rubenernst.iot.controller.domain.NodeMessage;
import nl.rubenernst.iot.controller.domain.Sensor;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.gateways.Gateway;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.OutputStream;
import java.util.Optional;

@Component
public class NodeMessageConverter implements Converter {

    @Getter
    private final Observable<Pair<NodeMessage, OutputStream>> messages;

    @Autowired
    public NodeMessageConverter(Gateway gateway, NodeManager nodeManager) {
        messages = gateway.getGateway()
                .map(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();
                    int childSensorId = message.getSensorId();

                    Node node = nodeManager.getNode(nodeId);
                    Optional<Sensor> sensor = node.getSensor(childSensorId);

                    NodeMessage nodeMessage = new NodeMessage(node, sensor.orElse(null), message.getMessageType(), message.getMessageSubType(), message.getAck(), message.getPayload());
                    return new Pair<>(nodeMessage, pair.getValue1());
                })
                .share();
    }
}
