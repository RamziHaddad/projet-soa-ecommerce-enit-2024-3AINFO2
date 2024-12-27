package com.microservices.order_service.config;

import com.microservices.order_service.dto.OrderStatusUpdateDTO;
import com.microservices.order_service.serialization.OrderStatusUpdateDTOSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderStatusUpdateProducerConfig {

    @Bean
    public ProducerFactory<String, OrderStatusUpdateDTO> OrderStatusUpdateProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderStatusUpdateDTOSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderStatusUpdateDTO> OrderStatusUpdateKafkaTemplate() {
        return new KafkaTemplate<>(OrderStatusUpdateProducerFactory());
    }
}