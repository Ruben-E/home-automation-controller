package nl.rubenernst.iot.controller.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import nl.rubenernst.iot.controller.domain.messages.SetReqMessageSubType;
import nl.rubenernst.iot.controller.domain.nodes.Node;
import nl.rubenernst.iot.controller.domain.nodes.Sensor;

import java.util.Date;

@Data
@Builder
public class Measurement {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
    public Date date;
    public Node node;
    public Sensor sensor;
    public SetReqMessageSubType type;
    public String data;
    public Integer dataAsInteger;
    public Float dataAsFloat;
}
