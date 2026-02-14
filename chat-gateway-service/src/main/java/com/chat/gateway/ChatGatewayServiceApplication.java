package com.chat.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class ChatGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGatewayServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner testRedis(StringRedisTemplate redisTemplate) {
        return args -> {
            redisTemplate.opsForValue().set("test:key", "connected");
            String value = redisTemplate.opsForValue().get("test:key");
            System.out.println("Redis test value: " + value);
        };
    }
}