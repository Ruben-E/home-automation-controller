package nl.rubenernst.iot.controller.domain;

import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Node {
    private final int id;
    private NodeType nodeType;
    private String name;
    private String version;
    @Singular
    private List<Sensor> sensors = new ArrayList<>();

    public Optional<Sensor> getSensor(int id) {
        return getSensors().stream().filter(sensor -> sensor.getId() == id).findFirst();
    }
}
