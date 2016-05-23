package nl.rubenernst.iot.controller.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sensor {
    private final int id;
    private SensorType sensorType;
}
