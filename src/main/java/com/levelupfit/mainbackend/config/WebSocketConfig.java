package com.levelupfit.mainbackend.config;

import com.levelupfit.mainbackend.handler.FeedbackWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
    private final FeedbackWebSocketHandler feedbackWebSocketHandler;

    public WebSocketConfig(FeedbackWebSocketHandler handler) {
        this.feedbackWebSocketHandler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(feedbackWebSocketHandler, "/ws/feedback/{feedbackId}")
                .setAllowedOrigins("http://localhost:4000","https://levelupfit.treebomb.org"); // CORS 허용 - 필요시 도메인 제한 가능
    }
}
