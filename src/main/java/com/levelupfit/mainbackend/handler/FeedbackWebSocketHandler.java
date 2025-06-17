package com.levelupfit.mainbackend.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FeedbackWebSocketHandler extends TextWebSocketHandler {
    // 연결된 세션을 feedbackId 기준으로 저장
    private final Map<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(FeedbackWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        int feedbackId = extractFeedbackId(session);
        sessionMap.put(feedbackId, session);
        System.out.println("WebSocket 연결됨: " + feedbackId);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session,@NotNull CloseStatus status) {
        int feedbackId = extractFeedbackId(session);
        sessionMap.remove(feedbackId);
        System.out.println("WebSocket 연결 종료: " + feedbackId);
    }

    // 서버에서 분석 완료되었을 때 호출
    public void sendAnalysisCompleteMessage(int feedbackId) {
        WebSocketSession session = sessionMap.get(feedbackId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("{\"type\": \"FEEDBACK_ANALYSIS_COMPLETE\"}"));
                session.close(); // 전송 후 종료
            } catch (IOException e) {
                logger.error("WebSocket 메시지 전송 중 오류 발생", e); // 로깅 처리 권장
            }
        }
    }

    // 피드백 ID 추출 로직
    private int extractFeedbackId(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        String idStr = uri.substring(uri.lastIndexOf("/") + 1);
        return (int) Long.parseLong(idStr);
    }
}
