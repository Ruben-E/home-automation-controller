package nl.rubenernst.iot.controller.domain.mysensors;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InternalMessage extends Message {
    public InternalMessage(int nodeId, int childSensorId, InternalMessageSubType messageSubType, int ack, String payload) {
        super(nodeId, childSensorId, MessageType.INTERNAL, messageSubType, ack, payload);
    }
}
