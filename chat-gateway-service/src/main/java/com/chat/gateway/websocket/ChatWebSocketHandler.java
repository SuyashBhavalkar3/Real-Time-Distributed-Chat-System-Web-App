package com.chat.gateway.websocket;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserId(session);
        sessions.put(userId, session);
        log.info("User connected: {}", userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserId(session);
        sessions.remove(userId);
        log.info("User disconnected: {}", userId);
    }

    private String getUserId(WebSocketSession session) {
        var uri = session.getUri();
        if (uri == null) {
            return "unknown";
        }
        String query = uri.getQuery();
        if (query == null) {
            return "unknown";
        }
        String[] parts = query.split("=");
        return parts.length > 1 ? parts[1] : "unknown";
    }

    public void sendToUser(String userId, String message) {
        WebSocketSession session = sessions.get(userId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("WebSocket send failed", e);
            }
        }
    }
}