package nl.rubenernst.iot.controller.domain.messages;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SetMessage extends Message {
    public SetMessage(int nodeId, int childSensorId, SetReqMessageSubType setReqMessageSubType, int ack, String payload) {
        super(nodeId, childSensorId, MessageType.SET, setReqMessageSubType, ack, payload);
    }
}
