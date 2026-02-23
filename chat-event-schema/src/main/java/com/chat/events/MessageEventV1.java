package main.java.com.chat.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Version 1 of MessageEvent.
 * This class is IMMUTABLE and SAFE for Kafka replay.
 */
public record MessageEventV1(

        // --- BaseEvent fields ---
        UUID eventId,
        Instant occurredAt,
        String schemaVersion,

        // --- Message-specific fields ---
        UUID messageId,
        UUID conversationId,
        UUID senderId,
        UUID recipientId,
        String content

) implements BaseEvent {

    @Override
    public String eventType() {
        return "MESSAGE";
    }
}