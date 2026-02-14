package com.chat.message_service.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {

    @NotNull
    private UUID conversationId;

    @NotNull
    private UUID senderId;

    @NotNull
    private UUID recipientId;

    @NotBlank
    private String content;
}