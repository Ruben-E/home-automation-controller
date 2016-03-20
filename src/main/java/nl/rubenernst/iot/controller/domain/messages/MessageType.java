package nl.rubenernst.iot.controller.domain.messages;

import lombok.Getter;

@Getter
public enum MessageType {
    PRESENTATION(0),
    SET(1),
    REQ(2),
    INTERNAL(3),
    STREAM(4);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public static MessageType fromInt(int value){
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getValue() == value) {
                return messageType;
            }
        }
        return null;
    }
}
