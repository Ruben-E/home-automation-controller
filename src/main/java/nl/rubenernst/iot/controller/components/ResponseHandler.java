package nl.rubenernst.iot.controller.components;

import lombok.extern.slf4j.Slf4j;
import nl.rubenernst.iot.controller.domain.messages.Message;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
@Slf4j
public class ResponseHandler {
    public void sendResponse(Message message, OutputStream outputStream) {
        try {
            outputStream.write(message.toResponseString().getBytes());
        } catch (Exception e) {
            log.warn("Could not send response", e);
        }
    }
}
