package org.soa.Kafka.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@ApplicationScoped
public class KafkaConfig {
    public Properties producerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.soa.cart.serialization.CartSerializer");
        return props;
    }
}
