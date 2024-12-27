package com.microservices.order_service.serialization;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.order_service.events.OrderPaidEvent;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderPaidEventSerializer implements Serializer<OrderPaidEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(OrderPaidEventSerializer.class);

    @Override
    public byte[] serialize(String topic, OrderPaidEvent data) {
        try {
            if (data == null) {
                logger.warn("Null received at serializing");
                return null;
            }
            logger.debug("Serializing OrderPaidEvent: {}", data);
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            logger.error("Error when serializing OrderPaidEvent to byte[]", e);
            throw new RuntimeException("Error when serializing OrderPaidEvent to byte[]", e);
        }
    }
}