package nl.rubenernst.iot.controller.components.observables;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.domain.messages.SetReqMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorMeasurement;
import nl.rubenernst.iot.controller.nodes.NodeManager;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Optional;

@Component
@Slf4j
public class SensorMeasurementObservable implements ControllerObservable {
    @Getter
    private Observable<Triplet<Node, Optional<Sensor>, SensorMeasurement>> observable;

    @Autowired
    public SensorMeasurementObservable(SerialReaderObservable serialReaderObservable, NodeManager nodeManager) {
        observable = serialReaderObservable.getObservable()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.SET &&
                            message.getSensorId() < 255;
                })
                .map(pair -> {
                    Message message = pair.getValue0();
                    int nodeId = message.getNodeId();
                    int childSensorId = message.getSensorId();

                    Node node = nodeManager.getNode(nodeId);
                    Optional<Sensor> sensor = node.getSensor(childSensorId);
                    SensorMeasurement sensorMeasurement = new SensorMeasurement((SetReqMessageSubType) message.getMessageSubType(), message.getPayload());

                    return new Triplet<>(node, sensor, sensorMeasurement);
                })
                .share();
    }
}
