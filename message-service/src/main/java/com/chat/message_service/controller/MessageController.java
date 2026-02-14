package com.chat.message_service.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

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

        MessageEvent event = MessageEvent.builder()
                .conversationId(request.getConversationId())
                .messageId(messageId)
                .senderId(request.getSenderId())
                .recipientId(request.getRecipientId())
                .content(request.getContent())
                .createdAt(Instant.now())
                .build();

        String eventJson = objectMapper.writeValueAsString(event);

        kafkaProducerService.sendMessage(
                request.getConversationId(),
                eventJson
        );

        return "Message queued with ID: " + messageId;
    }
}