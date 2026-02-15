package com.chat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ChatGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatGatewayServiceApplication.class, args);
    }
}
