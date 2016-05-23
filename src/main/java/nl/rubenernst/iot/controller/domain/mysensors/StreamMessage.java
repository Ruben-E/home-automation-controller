package nl.rubenernst.iot.controller.domain.mysensors;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class StreamMessage extends Message {
    public StreamMessage(int nodeId, int childSensorId, int ack, String payload) {
        super(nodeId, childSensorId, MessageType.STREAM, null, ack, payload);
    }
}
