package nl.rubenernst.iot.controller.components.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.SensorMeasurementObservable;
import nl.rubenernst.iot.controller.domain.Measurement;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorMeasurement;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SensorMeasurementHandler {
    @Autowired
    public SensorMeasurementHandler(SensorMeasurementObservable sensorMeasurementObservable, TransportClient transportClient, ObjectMapper objectMapper) {
        sensorMeasurementObservable.getObservable()
                .subscribe(triplet -> {
                    try {
                        Node node = triplet.getValue0();
                        Optional<Sensor> sensor = triplet.getValue1();
                        SensorMeasurement sensorMeasurement = triplet.getValue2();

                        Measurement measurement = Measurement.builder()
                                .node(node)
                                .sensor(sensor.isPresent() ? sensor.get() : null)
                                .type(sensorMeasurement.getReqMessageSubType())
                                .data(sensorMeasurement.getPayload())
                                .build();

                        transportClient.prepareIndex("measurements", "measurement")
                                .setOpType(IndexRequest.OpType.CREATE)
                                .setSource(objectMapper.writeValueAsString(measurement))
                                .get();
                    } catch (Exception e) {
                        log.error("Cannot write to elasticsearch", e);
                    }
                });
    }
}
