package com.chat.message_service.event;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent {

    private UUID conversationId;
    private UUID messageId;
    private UUID senderId;
    private String content;
    private Instant createdAt;
}