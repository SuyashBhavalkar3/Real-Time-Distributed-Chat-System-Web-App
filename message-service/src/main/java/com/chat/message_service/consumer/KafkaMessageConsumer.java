package com.chat.message_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.chat.message_service.entity.MessageEntity;
import com.chat.message_service.event.MessageEvent;
import com.chat.message_service.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageConsumer {

    private final ObjectMapper objectMapper;
    private final MessageRepository messageRepository;

    @KafkaListener(
            topics = "chat-messages",
            groupId = "message-group"
    )
    public void consume(String messageJson) {

        try {
            // Deserialize JSON → Event
            MessageEvent event =
                    objectMapper.readValue(messageJson, MessageEvent.class);

            // Convert Event → Entity
            MessageEntity entity = MessageEntity.builder()
                    .conversationId(event.getConversationId())
                    .messageId(event.getMessageId())
                    .senderId(event.getSenderId())
                    .content(event.getContent())
                    .createdAt(event.getCreatedAt())
                    .build();

            // Persist to Cassandra
            messageRepository.save(entity);

            log.info("Message persisted: {}", event.getMessageId());

        } catch (JsonProcessingException e) {
            log.error("Failed to process message", e);
            throw new RuntimeException(e);
        }
    }
}