package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.OrderEventDTO;
import com.microservices.order_service.dto.OrderStatusUpdateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class OrderStatusUpdateProducer {

    private static final Logger logger = Logger.getLogger(OrderStatusUpdateProducer.class.getName());

    private final KafkaTemplate<String, OrderStatusUpdateDTO> kafkaTemplate;

    @Value("order-status-update")
    private String topicName;

    public OrderStatusUpdateProducer(KafkaTemplate<String, OrderStatusUpdateDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderStatusUpdateDTO orderStatusUpdateDTO) {

        logger.info("Sending order event: " + orderStatusUpdateDTO);
        kafkaTemplate.send(topicName, orderStatusUpdateDTO);
    }
}
