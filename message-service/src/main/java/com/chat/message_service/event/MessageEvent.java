package com.chat.message_service.event;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageEvent {

    private UUID messageId;
    private UUID conversationId;
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private Instant createdAt;
}