package nl.rubenernst.iot.controller.filters;

import lombok.Getter;
import nl.rubenernst.iot.controller.domain.mysensors.Message;
import nl.rubenernst.iot.controller.domain.mysensors.MessageType;
import nl.rubenernst.iot.controller.domain.mysensors.PresentationMessageSubType;
import nl.rubenernst.iot.controller.gateways.Gateway;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.io.OutputStream;

@Component
public class NodeTypeFilter implements Filter {

    @Getter
    private final Observable<Pair<Message, OutputStream>> messages;

    @Autowired
    public NodeTypeFilter(Gateway gateway) {
        messages = gateway.getGateway()
                .filter(pair -> {
                    Message message = pair.getValue0();
                    return message.getMessageType() == MessageType.PRESENTATION &&
                            message.getMessageSubType() == PresentationMessageSubType.S_ARDUINO_NODE &&
                            message.getSensorId() == 255;
                })
                .share();
    }
}
