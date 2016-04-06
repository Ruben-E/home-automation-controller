package nl.rubenernst.iot.controller.gateways;

import nl.rubenernst.iot.controller.domain.messages.Message;
import org.javatuples.Pair;
import rx.Observable;

import java.io.OutputStream;

public interface Gateway {
    Observable<Pair<Message, OutputStream>> getGateway();
}
