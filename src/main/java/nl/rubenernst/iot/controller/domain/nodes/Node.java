package nl.rubenernst.iot.controller.domain.nodes;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Node {
    private final int id;
    private NodeType nodeType;
    private String name;
    private String version;
    private List<Sensor> sensors;

    public List<Sensor> getSensors() {
        if (sensors == null) {
            sensors = new ArrayList<>();
        }

        return sensors;
    }

    public Optional<Sensor> getSensor(int id) {
        return sensors.stream().filter(sensor -> sensor.getId() == id).findFirst();
    }
}
