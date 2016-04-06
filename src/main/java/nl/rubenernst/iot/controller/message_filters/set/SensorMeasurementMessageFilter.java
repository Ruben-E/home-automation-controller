package nl.rubenernst.iot.controller.message_filters.set;

import lombok.Getter;
import nl.rubenernst.iot.controller.domain.messages.Message;
import nl.rubenernst.iot.controller.domain.messages.MessageType;
import nl.rubenernst.iot.controller.gateways.Gateway;
import nl.rubenernst.iot.controller.message_filters.MessageFilter;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.OutputStream;

@Component
public class SensorMeasurementMessageFilter implements MessageFilter{

    @Getter
    private final Observable<Pair<Message, OutputStream>> messages;

    @Autowired
    public SensorMeasurementMessageFilter(Gateway gateway) {
        messages = gateway.getGateway()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.SET &&
                            message.getSensorId() < 255;
                })
                .share();
    }
}
