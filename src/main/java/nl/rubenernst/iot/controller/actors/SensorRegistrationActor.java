package nl.rubenernst.iot.controller.actors;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.Sensor;
import nl.rubenernst.iot.controller.domain.SensorType;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.domain.mysensors.PresentationMessageSubType;
import nl.rubenernst.iot.controller.filters.SensorPresentationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SensorRegistrationActor {
    @Autowired
    public SensorRegistrationActor(SensorPresentationFilter sensorPresentationFilter, NodeManager nodeManager, ExceptionHandler exceptionHandler) {
        sensorPresentationFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();
                    int childSensorId = message.getSensorId();

                    nodeManager.addSensor(nodeId, new Sensor(childSensorId, SensorType.fromSubType((PresentationMessageSubType) message.getMessageSubType())));
                }, exceptionHandler);
    }
}
