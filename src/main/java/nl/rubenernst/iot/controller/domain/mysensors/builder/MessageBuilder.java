package nl.rubenernst.iot.controller.domain.mysensors.builder;

import nl.rubenernst.iot.controller.domain.mysensors.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageBuilder {
    public List<Message> fromPayload(String payload) {
        List<Message> messages = new ArrayList<>();
        String[] messagePayloads = payload.split("\n");
        for (String s : messagePayloads) {
            Message message = null;

            String[] messageParts = s.split(";");
            int nodeId = Integer.valueOf(messageParts[0]);
            int childSensorId = Integer.valueOf(messageParts[1]);
            int messageTypeValue = Integer.valueOf(messageParts[2]);
            int ack = Integer.valueOf(messageParts[3]);
            int subTypeValue = Integer.valueOf(messageParts[4]);

            String messagePayload = null;
            if (messageParts.length == 6) {
                messagePayload = messageParts[5];
            }

            MessageType messageType = MessageType.fromInt(messageTypeValue);
            if (messageType != null) {
                switch (messageType) {
                    case PRESENTATION:
                        message = new PresentationMessage(nodeId, childSensorId, PresentationMessageSubType.fromInt(subTypeValue), ack, messagePayload);
                        break;
                    case SET:
                        message = new SetMessage(nodeId, childSensorId, SetReqMessageSubType.fromInt(subTypeValue), ack, messagePayload);
                        break;
                    case REQ:
                        message = new ReqMessage(nodeId, childSensorId, SetReqMessageSubType.fromInt(subTypeValue), ack, messagePayload);
                        break;
                    case INTERNAL:
                        message = new InternalMessage(nodeId, childSensorId, InternalMessageSubType.fromInt(subTypeValue), ack, messagePayload);
                        break;
                    case STREAM:
                        message = new StreamMessage(nodeId, childSensorId, ack, messagePayload);
                        break;
                }
            }

            messages.add(message);
        }

        return messages;
    }
}
