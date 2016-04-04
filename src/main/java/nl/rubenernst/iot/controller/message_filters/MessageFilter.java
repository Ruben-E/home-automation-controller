package nl.rubenernst.iot.controller.message_filters;

import nl.rubenernst.iot.controller.domain.messages.Message;
import org.javatuples.Pair;
import rx.Observable;

import java.io.OutputStream;

public interface MessageFilter {
    Observable<Pair<Message, OutputStream>> getMessages();
}
