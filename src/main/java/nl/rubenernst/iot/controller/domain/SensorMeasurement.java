package nl.rubenernst.iot.controller.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rubenernst.iot.controller.domain.mysensors.SetReqMessageSubType;

@Data
@AllArgsConstructor
public class SensorMeasurement {
    private final SetReqMessageSubType reqMessageSubType;
    private String payload;
}
