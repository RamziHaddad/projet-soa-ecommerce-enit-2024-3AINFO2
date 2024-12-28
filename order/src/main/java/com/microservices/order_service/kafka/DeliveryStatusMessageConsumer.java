package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.DeliveryStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class DeliveryStatusMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryStatusMessageConsumer.class);

    @KafkaListener(topics = "delivery-status", groupId = "deliveryReceiver", containerFactory = "kafkaListenerContainerFactory")
    public void listen(DeliveryStatusMessage deliveryStatusMessage) {

        logger.info("Received record: " + deliveryStatusMessage.getOrderId());
        logger.info("Delivery status: " + deliveryStatusMessage.getStatus());
    }


}
