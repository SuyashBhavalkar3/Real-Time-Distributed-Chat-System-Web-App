package com.chat.message_service.consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.chat.message_service.entity.MessageEntity;
import com.chat.events.MessageEventV1;
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
        MessageEventV1 event =
            objectMapper.readValue(messageJson, MessageEventV1.class);
        MessageEntity entity = MessageEntity.builder()
            .conversationId(event.conversationId())
            .messageId(event.messageId())
            .senderId(event.senderId())
            .content(event.content())
            .createdAt(event.occurredAt())
            .build();
        messageRepository.save(entity);
        log.info("Message persisted: {}", event.messageId());
    } catch (JsonProcessingException e) {
        log.error("JSON deserialization failed", e);
        throw new RuntimeException(e); // triggers retry + DLT
    } catch (org.springframework.dao.DataAccessException e) {
        log.error("Cassandra persistence failed", e);
        throw e; // already runtime, triggers retry
        }
    }
}