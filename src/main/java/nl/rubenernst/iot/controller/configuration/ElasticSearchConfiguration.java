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
    public TransportClient client(@Value("${elasticsearch.cluster.name}") String clusterName, @Value("${elasticsearch.cluster.host}") String host) throws UnknownHostException {
        transportClient = TransportClient.builder()
                .settings(Settings.builder()
                        .put("cluster.name", clusterName)
                        .build())
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
        return transportClient;
    }

    @PreDestroy
    public void destroy() {
        transportClient.close();
    }
}
