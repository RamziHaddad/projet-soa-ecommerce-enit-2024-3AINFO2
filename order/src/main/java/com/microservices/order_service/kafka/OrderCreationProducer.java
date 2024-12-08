package com.microservices.order_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.order_service.dto.OrderEventDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderCreationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreationProducer.class);

    private final NewTopic topic;
    private final KafkaTemplate<String, String> kafkaTemplate; // Note: String for value type
    private final ObjectMapper objectMapper;

    public OrderCreationProducer(NewTopic topic, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(OrderEventDTO orderEventDTO) {
        try {
            // Serialize the OrderEventDTO object to a JSON string
            String eventAsString = objectMapper.writeValueAsString(orderEventDTO);
            LOGGER.info("Order event (as JSON string) => {}", eventAsString);

            // Build a message with the serialized string
            Message<String> message = MessageBuilder
                    .withPayload(eventAsString)
                    .setHeader(KafkaHeaders.TOPIC, topic.name())
                    .build();

            // Send the message
            kafkaTemplate.send(message);

        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize OrderEventDTO: {}", e.getMessage(), e);
        }
    }
}
