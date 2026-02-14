package com.chat.gateway.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.chat.gateway.event.MessageEvent;
import com.chat.gateway.websocket.ChatWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketKafkaConsumer {

    private final ChatWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "chat-messages",
        groupId = "websocket-group"
)
public void consume(String messageJson) {

    try {
        MessageEvent event =
                objectMapper.readValue(messageJson, MessageEvent.class);

        String recipient = event.getRecipientId().toString();

        log.info("Trying to deliver to {}", recipient);

        webSocketHandler.sendToUser(recipient, messageJson);

        log.info("Delivered message {} to {}",
                event.getMessageId(),
                recipient);

    } catch (Exception e) {
        log.error("Kafka WebSocket consumer failed", e);
    }
}

}