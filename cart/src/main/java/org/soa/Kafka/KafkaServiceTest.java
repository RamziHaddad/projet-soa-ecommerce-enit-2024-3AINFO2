package org.soa.Kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KafkaServiceTest {

    @Inject
    KafkaProducerService producerService;

    @Inject
    KafkaConsumerService consumerService;

    public void testKafka() {
        // Envoi d'un message
        producerService.sendMessage("my-topic", "my-key", "Hello Kafka!");

        // Polling pour recevoir le message
        consumerService.pollMessages();
    }
}
