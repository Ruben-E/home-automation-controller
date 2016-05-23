package nl.rubenernst.iot.controller.domain;

import lombok.Getter;
import nl.rubenernst.iot.controller.domain.mysensors.PresentationMessageSubType;

@Getter
public enum SensorType {
    DOOR_SENSOR(PresentationMessageSubType.S_DOOR),
    MOTION_SENSOR(PresentationMessageSubType.S_MOTION),
    SMOKE_SENSOR(PresentationMessageSubType.S_SMOKE),
    LIGHT_ACTUATOR(PresentationMessageSubType.S_BINARY),
    DIMMER_ACTUATOR(PresentationMessageSubType.S_DIMMER),
    COVER_ACTUATOR(PresentationMessageSubType.S_COVER),
    TEMPERATURE_SENSOR(PresentationMessageSubType.S_TEMP),
    HUMIDITY_SENSOR(PresentationMessageSubType.S_HUM),
    BAROMETER_SENSOR(PresentationMessageSubType.S_BARO),
    WIND_SENSOR(PresentationMessageSubType.S_WIND),
    RAIN_SENSOR(PresentationMessageSubType.S_RAIN),
    UV_SENSOR(PresentationMessageSubType.S_UV),
    WEIGHT_SENSOR(PresentationMessageSubType.S_WEIGHT),
    POWER_SENSOR(PresentationMessageSubType.S_POWER),
    HEATER_ACTUATOR(PresentationMessageSubType.S_HEATER),
    DISTANCE_SENSOR(PresentationMessageSubType.S_DISTANCE),
    LIGHT_LEVEL_SENSOR(PresentationMessageSubType.S_LIGHT_LEVEL),
    LOCK_ACTUATOR(PresentationMessageSubType.S_LOCK),
    IR_ACTUATOR(PresentationMessageSubType.S_IR),
    WATER_SENSOR(PresentationMessageSubType.S_WATER),
    AIR_QUALITY_SENSOR(PresentationMessageSubType.S_AIR_QUALITY),
    CUSTOM_SENSOR(PresentationMessageSubType.S_CUSTOM),
    DUST_SENSOR(PresentationMessageSubType.S_DUST),
    SCENE_CONTROLLER_ACTUATOR(PresentationMessageSubType.S_SCENE_CONTROLLER),
    RGB_LIGHT_ACTUATOR(PresentationMessageSubType.S_RGB_LIGHT),
    RGBW_LIGHT_ACTUATOR(PresentationMessageSubType.S_RGBW_LIGHT),
    COLOR_SENSOR(PresentationMessageSubType.S_COLOR_SENSOR),
    HVAC_ACTUATOR(PresentationMessageSubType.S_HVAC),
    MULTIMETER_SENSOR(PresentationMessageSubType.S_MULTIMETER),
    SPRINKLER_ACTUATOR(PresentationMessageSubType.S_SPRINKLER),
    WATER_LEAK_SENSOR(PresentationMessageSubType.S_WATER_LEAK),
    SOUND_SENSOR(PresentationMessageSubType.S_SOUND),
    VIBRATION_SENSOR(PresentationMessageSubType.S_VIBRATION),
    MOISTURE_SENSOR(PresentationMessageSubType.S_MOISTURE);

    private PresentationMessageSubType presentationMessageSubType;

    SensorType(PresentationMessageSubType presentationMessageSubType) {
        this.presentationMessageSubType = presentationMessageSubType;
    }

    public static SensorType fromSubType(PresentationMessageSubType presentationMessageSubType){
        for (SensorType sensorType : SensorType.values()) {
            if (sensorType.getPresentationMessageSubType().equals(presentationMessageSubType)) {
                return sensorType;
            }
        }
        return null;
    }
}
