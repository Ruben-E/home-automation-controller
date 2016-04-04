package nl.rubenernst.iot.controller.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.components.observables.measurements.SensorMeasurementObservable;
import nl.rubenernst.iot.controller.domain.Measurement;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;
import nl.rubenernst.iot.controller.domain.nodes.SensorMeasurement;
import nl.rubenernst.iot.controller.exceptions.CannotPutMappingException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class SensorMeasurementHandler {
    @Autowired
    public SensorMeasurementHandler(boolean elasticSearchEnabled, SensorMeasurementObservable sensorMeasurementObservable, TransportClient transportClient, ObjectMapper objectMapper) throws CannotPutMappingException {
        if (elasticSearchEnabled) {
            putMapping(transportClient);

            sensorMeasurementObservable.getObservable()
                    .subscribe(triplet -> {
                        try {
                            Measurement measurement = getMeasurement(triplet);

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

    private void putMapping(TransportClient transportClient) throws CannotPutMappingException {
        try {
            if (!transportClient.admin().indices().prepareExists("measurements").get().isExists()) {
                transportClient.admin().indices().prepareCreate("measurements").get();

                transportClient.admin().indices().preparePutMapping("measurements")
                        .setType("measurement")
                        .setSource(
                                // @formatter:off
                                XContentFactory.jsonBuilder().prettyPrint()
                                    .startObject()
                                        .startObject("measurement")
                                            .startObject("_ttl").field("enabled", "true").field("default", "1y").endObject()
                                            .startObject("properties")
                                                .startObject("date").field("type", "date").field("format", "date_hour_minute_second_fraction").endObject()
                                                .startObject("node")
                                                    .startObject("properties")
                                                        .startObject("id").field("type", "integer").endObject()
                                                        .startObject("nodeType").field("type", "string").field("index", "not_analyzed").endObject()
                                                        .startObject("name").field("type", "string").field("index", "not_analyzed").endObject()
                                                        .startObject("version").field("type", "string").field("index", "not_analyzed").endObject()
                                                        .startObject("sensors")
                                                            .startObject("properties")
                                                                .startObject("id").field("type", "integer").endObject()
                                                                .startObject("sensorType").field("type", "string").field("index", "not_analyzed").endObject()
                                                            .endObject()
                                                        .endObject()
                                                    .endObject()
                                                .endObject()
                                                .startObject("sensor")
                                                    .startObject("properties")
                                                        .startObject("id").field("type", "integer").endObject()
                                                        .startObject("sensorType").field("type", "string").field("index", "not_analyzed").endObject()
                                                    .endObject()
                                                .endObject()
                                                .startObject("type").field("type", "string").field("index", "not_analyzed").endObject()
                                                .startObject("data").field("type", "string").field("index", "not_analyzed").endObject()
                                                .startObject("dataAsInteger").field("type", "integer").endObject()
                                                .startObject("dataAsFloat").field("type", "float").endObject()
                                            .endObject()
                                        .endObject()
                                .endObject()
                                // @formatter:on
                        )
                        .get();
            }
        } catch (Exception e) {
            throw new CannotPutMappingException("Cannot put the mapping", e);
        }
    }

    private Measurement getMeasurement(Triplet<Node, Optional<Sensor>, SensorMeasurement> triplet) {
        Node node = triplet.getValue0();
        Optional<Sensor> sensor = triplet.getValue1();
        SensorMeasurement sensorMeasurement = triplet.getValue2();

        String data = sensorMeasurement.getPayload();
        Integer dataAsInteger = null;
        try {
            dataAsInteger = Integer.parseInt(data);
        } catch (Exception ignored) {

        }

        Float dataAsFloat = null;
        try {
            dataAsFloat = new BigDecimal(data).floatValue();
        } catch (Exception ignored) {

        }

        return Measurement.builder()
                .date(new Date())
                .node(node)
                .sensor(sensor.isPresent() ? sensor.get() : null)
                .type(sensorMeasurement.getReqMessageSubType())
                .data(data)
                .dataAsInteger(dataAsInteger)
                .dataAsFloat(dataAsFloat)
                .build();
    }
}
