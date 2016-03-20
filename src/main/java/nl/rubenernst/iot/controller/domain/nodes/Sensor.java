package nl.rubenernst.iot.controller.domain.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sensor {
    private final int id;
    private SensorType sensorType;
}
