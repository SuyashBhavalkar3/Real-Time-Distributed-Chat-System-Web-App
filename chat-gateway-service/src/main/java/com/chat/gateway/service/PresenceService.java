package com.chat.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
        String key = "user:" + userId;
        redisTemplate.opsForValue().set(key, instanceId);
        System.out.println("Registered presence for user: " + userId);
    }

    public void removeUser(String userId) {
    String key = "user:" + userId;
    redisTemplate.delete(key);
    System.out.println("Removed presence for user: " + userId);
    }

}