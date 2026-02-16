package com.chat.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PresenceService {

    private final StringRedisTemplate redisTemplate;
    private final String instanceId;

    public PresenceService(StringRedisTemplate redisTemplate,
                           @Value("${gateway.instance-id}") String instanceId) {
        this.redisTemplate = redisTemplate;
        this.instanceId = instanceId;
    }

    public void registerUser(String userId) {
        redisTemplate.opsForValue()
                .set("user:" + userId, instanceId, Duration.ofSeconds(60));
    }

    public void removeUser(String userId) {
        redisTemplate.delete("user:" + userId);
    }

    public void refreshUser(String userId) {
        redisTemplate.expire("user:" + userId, Duration.ofSeconds(60));
    }

    public String getOwningInstance(String userId) {
        return redisTemplate.opsForValue().get("user:" + userId);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void refreshUser(String userId) {
        redisTemplate.expire("user:" + userId, Duration.ofSeconds(60));
    }
}