package org.soa.Kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.soa.Kafka.dto.CartDTO;

public class CartSerializer implements Serializer<CartDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, CartDTO cart) {
        try {
            return objectMapper.writeValueAsBytes(cart);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing CartDTO", e);
        }
    }
}
