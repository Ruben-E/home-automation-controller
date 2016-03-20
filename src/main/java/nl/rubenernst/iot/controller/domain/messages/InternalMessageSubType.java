package nl.rubenernst.iot.controller.domain.messages;

import lombok.Getter;

@Getter
public enum InternalMessageSubType implements MessageSubType {
    I_BATTERY_LEVEL(0),
    I_TIME(1),
    I_VERSION(2),
    I_ID_REQUEST(3),
    I_ID_RESPONSE(4),
    I_INCLUSION_MODE(5),
    I_CONFIG(6),
    I_FIND_PARENT(7),
    I_FIND_PARENT_RESPONSE(8),
    I_LOG_MESSAGE(9),
    I_CHILDREN(10),
    I_SKETCH_NAME(11),
    I_SKETCH_VERSION(12),
    I_REBOOT(13),
    I_GATEWAY_READY(14),
    I_REQUEST_SIGNING(15),
    I_GET_NONCE(16),
    I_GET_NONCE_RESPONSE(17);

    private int value;

    InternalMessageSubType(int value) {
        this.value = value;
    }

    public static InternalMessageSubType fromInt(int value) {
        for (InternalMessageSubType messageType : InternalMessageSubType.values()) {
            if (messageType.getValue() == value) {
                return messageType;
            }
        }
        return null;
    }
}
