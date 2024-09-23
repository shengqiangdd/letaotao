package com.gxcy.letaotao.web.app.config;


import com.gxcy.letaotao.web.app.config.handler.ChatWebSocketHandler;
import com.gxcy.letaotao.web.app.config.handler.NotificationWebSocketHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private ChatWebSocketHandler chatWebSocketHandler;
    @Resource
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { // 注册处理器
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .setAllowedOriginPatterns("*");
        registry.addHandler(notificationWebSocketHandler, "/ws/notification")
                .setAllowedOriginPatterns("*");
    }

}