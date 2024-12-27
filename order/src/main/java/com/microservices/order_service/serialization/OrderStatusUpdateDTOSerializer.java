package com.microservices.order_service.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.microservices.order_service.dto.OrderStatusUpdateDTO;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class OrderStatusUpdateDTOSerializer implements Serializer<OrderStatusUpdateDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, OrderStatusUpdateDTO data) {
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