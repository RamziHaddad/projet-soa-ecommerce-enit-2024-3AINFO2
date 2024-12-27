package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.OrderEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class OrderEventProducer {

    private static final Logger logger = Logger.getLogger(OrderEventProducer.class.getName());

    private final KafkaTemplate<String, OrderEventDTO> kafkaTemplate;

    @Value("order-creation")
    private String topicName;

    public OrderEventProducer(KafkaTemplate<String, OrderEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderEventDTO orderEvent) {

        logger.info("Sending order event: " + orderEvent);
        kafkaTemplate.send(topicName, orderEvent);
    }
}
