package com.chat.message_service.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.chat.message_service.entity.MessageEntity;

@Repository
public interface MessageRepository
        extends CassandraRepository<MessageEntity, UUID> {
}
