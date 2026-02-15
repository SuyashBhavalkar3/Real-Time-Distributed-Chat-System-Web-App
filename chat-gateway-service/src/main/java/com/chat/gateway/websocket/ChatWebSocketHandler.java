package com.chat.gateway.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.chat.gateway.service.PresenceService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final PresenceService presenceService;

    public ChatWebSocketHandler(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserId(session);

        if (userId == null) {
            log.warn("Connection rejected: missing userId");
            return;
        }

        sessions.put(userId, session);
        session.getAttributes().put("userId", userId);

        presenceService.registerUser(userId);

        log.info("User connected: {}", userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");

        if (userId != null) {
            sessions.remove(userId);
            presenceService.removeUser(userId);
            log.info("User disconnected: {}", userId);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void heartbeat() {
        for (String userId : sessions.keySet()) {
            presenceService.refreshUser(userId);
        }
    }


    private String extractUserId(WebSocketSession session) {
        if (session.getUri() == null) return null;

        String query = session.getUri().getQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("userId")) {
                return pair[1];
            }
        }
        return null;
    }



    public void sendToUser(String userId, String message) {
        WebSocketSession session = sessions.get(userId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("WebSocket send failed for user {}", userId, e);
            }
        }
    }
}