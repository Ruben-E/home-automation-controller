package nl.rubenernst.iot.controller.components.subscribers;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.SerialReaderObservable;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.domain.messages.PresentationMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorType;
import nl.rubenernst.iot.controller.nodes.NodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegisterSensorHandler {
    @Autowired
    public RegisterSensorHandler(SerialReaderObservable serialReaderObservable, NodeManager nodeManager) {
        serialReaderObservable.getSerialPortObservable()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.PRESENTATION &&
                            message.getSensorId() < 255;
                })
                .subscribe(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();
                    int childSensorId = message.getSensorId();

                    nodeManager.addSensor(nodeId, new Sensor(childSensorId, SensorType.fromSubType((PresentationMessageSubType) message.getMessageSubType())));
                });
    }
}
