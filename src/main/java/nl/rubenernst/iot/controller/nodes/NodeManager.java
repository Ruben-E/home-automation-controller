package nl.rubenernst.iot.controller.nodes;

import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.NodeType;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NodeManager {
    private List<Node> nodes = new ArrayList<>();
    private int latestId = 0;

    public synchronized Node createNode() {
        int newId = latestId++;
        return createNode(newId);
    }

    public synchronized Node createNode(int id) {
        Node node = new Node(id);

        nodes.add(node);
        if (id > latestId) {
            latestId = id;
        }

        return node;
    }

    public synchronized void setNodeType(int nodeId, NodeType nodeType) {
        Node node = getNode(nodeId);
        node.setNodeType(nodeType);
    }

    public synchronized void setName(int nodeId, String name) {
        Node node = getNode(nodeId);
        node.setName(name);
    }

    public synchronized void setVersion(int nodeId, String version) {
        Node node = getNode(nodeId);
        node.setVersion(version);
    }

    public synchronized void addSensor(int nodeId, Sensor sensor) {
        Node node = getNode(nodeId);
        Optional<Sensor> sensorOptional = node.getSensors().stream().filter(existingSensor -> existingSensor.getId() == sensor.getId()).findFirst();
        if (sensorOptional.isPresent()) {
            sensorOptional.get().setSensorType(sensor.getSensorType());
        } else {
            node.getSensors().add(sensor);
        }
    }

    public Node getNode(int nodeId) {
        Optional<Node> nodeOptional = nodes.stream().filter(node -> node.getId() == nodeId).findFirst();
        if (nodeOptional.isPresent()) {
            return nodeOptional.get();
        }

        return createNode(nodeId);
    }
}
