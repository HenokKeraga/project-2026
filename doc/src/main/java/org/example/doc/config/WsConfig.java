package org.example.doc.config;


import org.example.doc.service.DocWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WsConfig implements WebSocketConfigurer {

    private final DocWebSocketHandler handler;

    public WsConfig(DocWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/docs/{docId}")
                .setAllowedOrigins("*");
    }
}

