package com.chat.gateway.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.chat.gateway.event.MessageEvent;
import com.chat.gateway.service.PresenceService;
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
    private final PresenceService presenceService;

    @KafkaListener(topics = "chat-messages", groupId = "websocket-group")
    public void consume(String messageJson) {

        log.info("Kafka event received. Raw payload={}", messageJson);

        try {
            MessageEvent event =
                    objectMapper.readValue(messageJson, MessageEvent.class);

            String recipient = event.getRecipientId().toString();

            String owningInstance = presenceService.getOwningInstance(recipient);
            String currentInstance = presenceService.getInstanceId();

            if (owningInstance == null) {
                log.info("User {} is offline. Skipping delivery.", recipient);
                return;
            }

            if (!currentInstance.equals(owningInstance)) {
                log.info("Instance {} skipping delivery for user {} (owned by {})",
                        currentInstance, recipient, owningInstance);
                return;
            }

            webSocketHandler.sendToUser(recipient, messageJson);

            log.info("Delivered message {} to {} on instance {}",
                    event.getMessageId(), recipient, currentInstance);

        } catch (Exception e) {
            log.error("Failed to process Kafka message. Raw payload={}", messageJson, e);
        }
    }
}