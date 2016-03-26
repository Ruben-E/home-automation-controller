package nl.rubenernst.iot.controller.components.observables.gateway;

import nl.rubenernst.iot.controller.domain.messages.Message;
import org.javatuples.Pair;
import rx.Observable;

import java.io.OutputStream;

public interface GatewayObservable {
    Observable<Pair<Message, OutputStream>> getObservable();
}
