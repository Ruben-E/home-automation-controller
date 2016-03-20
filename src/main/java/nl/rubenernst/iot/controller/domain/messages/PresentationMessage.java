package nl.rubenernst.iot.controller.domain.messages;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class PresentationMessage extends Message {
    public PresentationMessage(int nodeId, int childSensorId, PresentationMessageSubType presentationMessageSubType, int ack, String payload) {
        super(nodeId, childSensorId, MessageType.PRESENTATION, presentationMessageSubType, ack, payload);
    }
}
