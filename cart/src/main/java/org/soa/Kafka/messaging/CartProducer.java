package org.soa.Kafka.messaging;

import jakarta.inject.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.soa.Kafka.dto.CartDTO;

import java.util.UUID;

public class CartProducer {
    @Inject
    KafkaConfig kafkaConfig;

    public void sendCartMessage(CartDTO cart) {
        try (KafkaProducer<String, CartDTO> producer = new KafkaProducer<>(kafkaConfig.producerConfig())) {
            ProducerRecord<String, CartDTO> record = new ProducerRecord<>("cart-topic", cart.getCartId().toString(), cart);
            producer.send(record);
            System.out.println("Cart published successfully!");
        }
    }
}
