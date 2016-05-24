package nl.rubenernst.iot.controller.configuration;

import com.microsoft.azure.iot.service.sdk.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class AzureConfiguration {
    private static ServiceClient serviceClient;

    @Bean
    public boolean azureEnabled(@Value("${azure.servicebus.enabled}") boolean enabled) {
        return enabled;
    }

    @Bean
    public ServiceClient serviceClient(boolean azureEnabled, @Value("${azure.servicebus.hostname}") String hostname, @Value("${azure.servicebus.access.key}") String accessKey, @Value("${azure.servicebus.access.key.name}") String accessKeyName) throws Exception {
        if (azureEnabled) {
            IotHubConnectionString connectionString = IotHubConnectionStringBuilder.createConnectionString("iot-hub-ruben.azure-devices.net", new ServiceAuthenticationWithSharedAccessPolicyKey(accessKeyName, accessKey));
            serviceClient = ServiceClient.createFromConnectionString(connectionString.toString(), IotHubServiceClientProtocol.AMQPS);
            serviceClient.open();

            return serviceClient;
        }

        return null;
    }

    @PreDestroy
    public void closeServiceClient() throws IOException {
        serviceClient.close();
    }
}
