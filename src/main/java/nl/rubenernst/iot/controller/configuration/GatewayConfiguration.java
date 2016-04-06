package nl.rubenernst.iot.controller.configuration;

import nl.rubenernst.iot.controller.domain.GatewayType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean
    public GatewayType gatewayType(@Value("${gateway.type}") String gatewayType) {
        return GatewayType.valueOf(gatewayType.toUpperCase());
    }
}
