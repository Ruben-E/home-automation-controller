package nl.rubenernst.iot.controller.configuration;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfiguration {
    private TransportClient transportClient;

    @Bean
    public boolean elasticSearchEnabled(@Value("${elasticsearch.enabled}") boolean enabled) {
        return enabled;
    }

    @Bean
    public TransportClient client(boolean elasticSearchEnabled, @Value("${elasticsearch.cluster.name}") String clusterName, @Value("${elasticsearch.cluster.host}") String host) throws UnknownHostException {
        if (elasticSearchEnabled) {
            transportClient = TransportClient.builder()
                    .settings(Settings.builder()
                            .put("cluster.name", clusterName)
                            .build())
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
            return transportClient;
        }

        return null;
    }

    @PreDestroy
    public void destroy() {
        if (transportClient != null) {
            transportClient.close();
        }
    }
}
