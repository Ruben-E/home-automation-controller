package nl.rubenernst.iot.controller.filters;

import lombok.Getter;
import nl.rubenernst.iot.controller.converters.NodeMessageConverter;
import nl.rubenernst.iot.controller.domain.NodeMessage;
import nl.rubenernst.iot.controller.domain.mysensors.MessageType;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.OutputStream;

@Component
public class MeasurementFilter implements Filter {

    @Getter
    private final Observable<Pair<NodeMessage, OutputStream>> messages;

    @Autowired
    public MeasurementFilter(NodeMessageConverter nodeMessageConverter) {
        messages = nodeMessageConverter.getMeasurements()
                .filter(pair -> {
                    NodeMessage nodeMessage = pair.getValue0();
                    return nodeMessage.getMessageType() == MessageType.SET &&
                            nodeMessage.getSensor() != null;
                })
                .share();
    }
}
