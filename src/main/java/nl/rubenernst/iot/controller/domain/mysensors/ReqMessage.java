package nl.rubenernst.iot.controller.domain.mysensors;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class ReqMessage extends Message {
    public ReqMessage(int nodeId, int childSensorId, SetReqMessageSubType setReqMessageSubType, int ack, String payload) {
        super(nodeId, childSensorId, MessageType.REQ, setReqMessageSubType, ack, payload);
    }
}
