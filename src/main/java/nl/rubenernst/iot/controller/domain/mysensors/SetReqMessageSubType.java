package nl.rubenernst.iot.controller.domain.mysensors;

import lombok.Getter;

@Getter
public enum SetReqMessageSubType implements MessageSubType {
    V_TEMP(0),
    V_HUM(1),
    V_STATUS(2),
    V_PERCENTAGE(3),
    V_PRESSURE(4),
    V_FORECAST(5),
    V_RAIN(6),
    V_RAINRATE(7),
    V_WIND(8),
    V_GUST(9),
    V_DIRECTION(10),
    V_UV(11),
    V_WEIGHT(12),
    V_DISTANCE(13),
    V_IMPEDANCE(14),
    V_ARMED(15),
    V_TRIPPED(16),
    V_WATT(17),
    V_KWH(18),
    V_SCENE_ON(19),
    V_SCENE_OFF(20),
    V_HVAC_FLOW_STATE(21),
    V_HVAC_SPEED(22),
    V_LIGHT_LEVEL(23),
    V_VAR1(24),
    V_VAR2(25),
    V_VAR3(26),
    V_VAR4(27),
    V_VAR5(28),
    V_UP(29),
    V_DOWN(30),
    V_STOP(31),
    V_IR_SEND(32),
    V_IR_RECEIVE(33),
    V_FLOW(34),
    V_VOLUME(35),
    V_LOCK_STATUS(36),
    V_LEVEL(37),
    V_VOLTAGE(38),
    V_CURRENT(39),
    V_RGB(40),
    V_RGBW(41),
    V_ID(42),
    V_UNIT_PREFIX(43),
    V_HVAC_SETPOINT_COOL(44),
    V_HVAC_SETPOINT_HEAT(45),
    V_HVAC_FLOW_MODE(46);

    private int value;

    SetReqMessageSubType(int value) {
        this.value = value;
    }

    public static SetReqMessageSubType fromInt(int value) {
        for (SetReqMessageSubType messageType : SetReqMessageSubType.values()) {
            if (messageType.getValue() == value) {
                return messageType;
            }
        }
        return null;
    }
}
