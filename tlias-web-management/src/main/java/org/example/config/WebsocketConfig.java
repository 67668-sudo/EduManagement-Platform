package org.example.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 这一句是灵魂，没有它，@ServerEndpoint 就是废纸一张
        return new ServerEndpointExporter();
    }
}