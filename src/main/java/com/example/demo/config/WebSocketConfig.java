package com.example.demo.config;

import com.example.demo.util.DepartmentWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DepartmentWebSocketHandler wsHandler;

    // 通过构造器注入，确保使用 Spring 容器中的单例
    public WebSocketConfig(DepartmentWebSocketHandler wsHandler) {
        this.wsHandler = wsHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(wsHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
