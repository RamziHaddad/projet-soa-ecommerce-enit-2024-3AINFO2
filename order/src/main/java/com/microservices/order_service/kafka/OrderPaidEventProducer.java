package com.microservices.order_service.kafka;

import com.microservices.order_service.events.OrderPaidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderPaidEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaidEventProducer.class);

    private final KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;
    private final String topic;

    public OrderPaidEventProducer(
            KafkaTemplate<String, OrderPaidEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.order-paid}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publishOrderPaidEvent(OrderPaidEvent event) {
        try {
            logger.info("Publishing OrderPaidEvent for orderId: {}", event.getOrderId());

            CompletableFuture<SendResult<String, OrderPaidEvent>> future =
                    kafkaTemplate.send(topic, event.getOrderId().toString(), event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to send OrderPaidEvent for orderId: {}",
                            event.getOrderId(), ex);
                    handleFailure(event, ex);
                } else {
                    handleSuccess(event, result);
                }
            });
        } catch (Exception e) {
            logger.error("Unexpected error while publishing OrderPaidEvent", e);
            throw new RuntimeException("Error publishing OrderPaidEvent", e);
        }
    }

    private void handleSuccess(OrderPaidEvent event, SendResult<String, OrderPaidEvent> result) {
        logger.info("Successfully sent OrderPaidEvent for orderId: {} | Topic: {} | Partition: {} | Offset: {}",
                event.getOrderId(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }

    private void handleFailure(OrderPaidEvent event, Throwable ex) {
        // Implement your failure handling strategy here
        // For example: retry logic, notifications, or storing failed events
        logger.error("Error sending OrderPaidEvent for orderId: {}", event.getOrderId(), ex);
    }
}