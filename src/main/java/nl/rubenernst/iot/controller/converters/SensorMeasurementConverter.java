package nl.rubenernst.iot.controller.converters;

import lombok.Getter;
import nl.rubenernst.iot.controller.data.NodeManager;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.SetReqMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorMeasurement;
import nl.rubenernst.iot.controller.message_filters.set.SensorMeasurementMessageFilter;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Optional;

@Component
public class SensorMeasurementConverter implements Converter {

    @Getter
    private final Observable<Triplet<Node, Optional<Sensor>, SensorMeasurement>> measurements;

    @Autowired
    public SensorMeasurementConverter(SensorMeasurementMessageFilter messageFilter, NodeManager nodeManager) {
        measurements = messageFilter.getMessages()
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
