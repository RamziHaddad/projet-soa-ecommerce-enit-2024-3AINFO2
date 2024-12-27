package com.microservices.order_service.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.order_service.dto.CartDTO;
import org.apache.kafka.common.serialization.Deserializer;


import java.util.Map;

public class CartDTODeserializer implements Deserializer<CartDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configure if needed
    }

    @Override
    public CartDTO deserialize(String topic, byte[] data) {
        try {
            // Convert the byte array into CartDTO object
            return objectMapper.readValue(data, CartDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize CartDTO", e);
        }
    }

    @Override
    public void close() {
        // Clean up if necessary
    }
}