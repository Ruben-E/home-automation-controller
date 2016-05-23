package nl.rubenernst.iot.controller.domain;

import lombok.Data;
import nl.rubenernst.iot.controller.domain.mysensors.MessageSubType;
import nl.rubenernst.iot.controller.domain.mysensors.MessageType;

@Data
public class NodeMessage {
    private final Node node;
    private final Sensor sensor;
    private final MessageType messageType;
    private final MessageSubType messageSubType;
    private final int ack;
    private final String payload;
}
