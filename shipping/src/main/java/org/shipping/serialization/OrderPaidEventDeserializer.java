package org.shipping.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.shipping.dto.OrderPaidEvent;

public class OrderPaidEventDeserializer extends ObjectMapperDeserializer<OrderPaidEvent> {
    // Public no-argument constructor
    public OrderPaidEventDeserializer() {
        // Specify the target class for deserialization
        super(OrderPaidEvent.class);
    }
}