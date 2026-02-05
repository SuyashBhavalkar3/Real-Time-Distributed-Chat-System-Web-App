package com.chat.message_service.controller;
import java.time.Instant;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.message_service.dto.MessageRequest;
import com.chat.message_service.event.MessageEvent;
import com.chat.message_service.service.KafkaProducerService;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public String sendMessage(@RequestBody MessageRequest request) throws Exception {
        UUID messageId = Uuids.timeBased();
        Instant now = Instant.now();

        MessageEvent event = MessageEvent.builder()
                .conversationId(request.getConversationId())
                .messageId(messageId)
                .senderId(request.getSenderId())
                .content(request.getContent())
                .createdAt(now)
                .build();

        String eventJson = objectMapper.writeValueAsString(event);

        kafkaProducerService.sendMessage(
                request.getConversationId(),
                eventJson
        );

        return "Message queued with ID: " + messageId;
    }
}
