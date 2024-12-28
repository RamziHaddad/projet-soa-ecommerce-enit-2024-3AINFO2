package com.microservices.order_service.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.order_service.dto.DeliveryStatusMessage;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class DeliveryStatusMessageDeserializer  implements Deserializer<DeliveryStatusMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configure if needed
    }

    @Override
    public DeliveryStatusMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, DeliveryStatusMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize DeliveryStatusMessage", e);
        }
    }

    @Override
    public void close() {
        // Clean up if necessary
    }
}
