package com.microservices.order_service.config;

import com.microservices.order_service.dto.CartDTO;
import com.microservices.order_service.dto.DeliveryStatusMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class DeliveryStatusMessageConsumerConfig {
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryStatusMessage> DeliveryListenerContainerFactory() {
        // Create the consumer configuration
        Map<String, Object> props = new LinkedHashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "deliveryReceiver");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Use JsonDeserializer for DeliveryStatusMessage
        JsonDeserializer<DeliveryStatusMessage> deliveryStatusMessageDeserializer = new JsonDeserializer<>(DeliveryStatusMessage.class);
        deliveryStatusMessageDeserializer.addTrustedPackages("*"); // Allow all packages, or restrict this if necessary

        // Create the consumer factory
        DefaultKafkaConsumerFactory<String, DeliveryStatusMessage> consumerFactory =
                new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deliveryStatusMessageDeserializer);

        // Create and configure the factory for concurrent Kafka listeners
        ConcurrentKafkaListenerContainerFactory<String, DeliveryStatusMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
