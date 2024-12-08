package com.microservices.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("order-creation")
    private String topicName;

    // kafka bean for kafka topic
    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(topicName).build();
    }
}
