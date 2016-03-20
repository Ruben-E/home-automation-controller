package nl.rubenernst.iot.controller.domain.messages;

import lombok.Data;

@Data
public class Message {
    private final int nodeId;
    private final int sensorId;
    private final MessageType messageType;
    private final MessageSubType messageSubType;
    private final int ack;
    private final String payload;

    public String toResponseString(){
        StringBuilder string = new StringBuilder("");
        string.append(nodeId).append(";");
        string.append(sensorId).append(";");
        string.append(messageType.getValue()).append(";");
        string.append(ack).append(";");
        string.append(messageSubType.getValue()).append(";");
        string.append(payload);
        string.append("\n");

        return string.toString();
    }
}
