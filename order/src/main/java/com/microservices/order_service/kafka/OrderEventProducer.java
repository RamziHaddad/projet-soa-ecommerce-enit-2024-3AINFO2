package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.OrderEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    @Value("order-creation")
    private String topicName;

    public OrderEventProducer(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderEventDTO orderEvent) {
        kafkaTemplate.send(topicName, orderEvent);
    }
}
