package com.microservices.order_service.config;

import com.microservices.order_service.events.OrderPaidEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;

import com.microservices.order_service.dto.OrderEventDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderPaidEventProducerConfig {

    @Bean
    public ProducerFactory<String, OrderPaidEvent> OrderPaidEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, com.microservices.order_service.serialization.OrderPaidEventSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderPaidEvent> OrderPaidEventKafkaTemplate() {
        return new KafkaTemplate<>(OrderPaidEventProducerFactory());
    }


}