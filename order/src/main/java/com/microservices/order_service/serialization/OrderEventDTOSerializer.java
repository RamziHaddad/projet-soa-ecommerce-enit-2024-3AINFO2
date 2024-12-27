package com.microservices.order_service.serialization;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.order_service.dto.OrderEventDTO;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class OrderEventDTOSerializer implements Serializer<OrderEventDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, OrderEventDTO data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing OrderEventDTO", e);
        }
    }

    @Override
    public void close() {
    }
}