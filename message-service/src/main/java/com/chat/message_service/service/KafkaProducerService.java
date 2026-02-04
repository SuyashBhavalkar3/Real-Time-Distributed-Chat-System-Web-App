package com.chat.message_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "chat-messages";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = Objects.requireNonNull(
                kafkaTemplate,
                "KafkaTemplate cannot be null"
        );
    }

    public void sendMessage(UUID conversationId, String messagePayload) {

        // Enforce null safety explicitly
        UUID safeConversationId = Objects.requireNonNull(
                conversationId,
                "conversationId cannot be null"
        );

        String safePayload = Objects.requireNonNull(
                messagePayload,
                "messagePayload cannot be null"
        );

        // Use conversationId as partition key for ordering guarantee
        String key = safeConversationId.toString();

        kafkaTemplate.send(TOPIC, key, safePayload);
    }
}
