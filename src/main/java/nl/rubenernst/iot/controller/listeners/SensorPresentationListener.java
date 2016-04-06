package nl.rubenernst.iot.controller.listeners;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.ExceptionHandler;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.PresentationMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorType;
import nl.rubenernst.iot.controller.message_filters.MessageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SensorPresentationListener {
    @Autowired
    public SensorPresentationListener(MessageFilter sensorPresentationMessageFilter, NodeManager nodeManager, ExceptionHandler exceptionHandler) {
        sensorPresentationMessageFilter.getMessages()
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();
                    int childSensorId = message.getSensorId();

                    nodeManager.addSensor(nodeId, new Sensor(childSensorId, SensorType.fromSubType((PresentationMessageSubType) message.getMessageSubType())));
                }, exceptionHandler);
    }
}
