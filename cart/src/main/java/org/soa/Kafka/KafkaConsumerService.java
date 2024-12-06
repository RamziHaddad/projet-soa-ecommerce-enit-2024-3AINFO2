package org.soa.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@ApplicationScoped
public class KafkaConsumerService {

    private KafkaConsumer<String, String> consumer;

    public KafkaConsumerService() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "my-group");
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList("my-topic"));
    }

    public void pollMessages() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        records.forEach(record -> {
            System.out.printf("Message reçu : key=%s, value=%s, topic=%s%n",
                    record.key(), record.value(), record.topic());
        });
    }
}
