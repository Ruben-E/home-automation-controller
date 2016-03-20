package nl.rubenernst.iot.controller.domain.nodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rubenernst.iot.controller.domain.messages.SetReqMessageSubType;

@Data
@AllArgsConstructor
public class SensorMeasurement {
    private final SetReqMessageSubType reqMessageSubType;
    private String payload;
}
