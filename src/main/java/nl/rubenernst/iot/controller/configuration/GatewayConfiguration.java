package nl.rubenernst.iot.controller.configuration;

import nl.rubenernst.iot.controller.components.observables.gateway.GatewayObservable;
import nl.rubenernst.iot.controller.components.observables.gateway.EthernetGatewayObservable;
import nl.rubenernst.iot.controller.components.observables.gateway.SerialGatewayObservable;
import nl.rubenernst.iot.controller.domain.GatewayType;
import nl.rubenernst.iot.controller.domain.messages.builder.MessageBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

@Configuration
public class GatewayConfiguration {
    @Bean
    public GatewayType gatewayType(@Value("${gateway.type}") String gatewayType) {
        return GatewayType.valueOf(gatewayType.toUpperCase());
    }

    @Bean
    public GatewayObservable gateway(GatewayType gatewayType, MessageBuilder messageBuilder, ExecutorService executorService, @Value("${gateway.ip}") String gatewayIp, @Value("${gateway.port}") int gatewayPort) {
        if (gatewayType == GatewayType.ETHERNET) {
            return constructEthernetGatewayObservable(messageBuilder, executorService, gatewayIp, gatewayPort);
        } else if (gatewayType == GatewayType.SERIAL) {
            return constructSerialGatewayObservable(messageBuilder, executorService);
        }

        throw new IllegalArgumentException("No gateway observable found for [" + gatewayType + "]");
    }

    private GatewayObservable constructSerialGatewayObservable(MessageBuilder messageBuilder, ExecutorService executorService) {
        return new SerialGatewayObservable(messageBuilder, executorService);
    }

    private GatewayObservable constructEthernetGatewayObservable(MessageBuilder messageBuilder, ExecutorService executorService, String gatewayIp, int gatewayPort) {
        return new EthernetGatewayObservable(messageBuilder, executorService, gatewayIp, gatewayPort);
    }
}
