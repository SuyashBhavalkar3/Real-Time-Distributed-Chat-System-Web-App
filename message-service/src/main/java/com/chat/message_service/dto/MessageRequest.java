package com.chat.message_service.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class MessageRequest {

    private UUID conversationId;
    private UUID senderId;
    private String content;
}
