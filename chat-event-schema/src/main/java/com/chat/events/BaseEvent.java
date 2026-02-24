package com.chat.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Base contract for all Kafka events.
 * This interface MUST remain backward compatible forever.
 */
public interface BaseEvent {

    /** Unique event id (not message id) */
    UUID eventId();

    /** When the event was created */
    Instant occurredAt();

    /** Logical event type (MESSAGE, PRESENCE, etc.) */
    String eventType();

    /** Explicit schema version (v1, v2, ...) */
    String schemaVersion();
}