package com.chat.message_service.entity;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @PrimaryKeyColumn(
            name = "conversation_id",
            ordinal = 0,
            type = PrimaryKeyType.PARTITIONED
    )
    private UUID conversationId;

    @PrimaryKeyColumn(
            name = "message_id",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED
    )
    private UUID messageId;

    @Column("sender_id")
    private UUID senderId;

    @Column("content")
    private String content;

    @Column("created_at")
    private Instant createdAt;
}
