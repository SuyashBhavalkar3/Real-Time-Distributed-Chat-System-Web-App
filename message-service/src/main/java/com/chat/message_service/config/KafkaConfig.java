package com.chat.message_service.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) ->
                                new TopicPartition(
                                        "chat-messages-dlt",
                                        record.partition()
                                )
                );

        ExponentialBackOffWithMaxRetries backOff =
                new ExponentialBackOffWithMaxRetries(3);

        backOff.setInitialInterval(500L);   // 0.5s
        backOff.setMultiplier(2.0);        // exponential
        backOff.setMaxInterval(5000L);     // cap at 5s

        return new DefaultErrorHandler(recoverer, backOff);
    }
}