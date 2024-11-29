package enit.ecomerce.search_product.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import enit.ecomerce.search_product.consumer.ProductListed;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@EnableRetry
@EnableScheduling
@Configuration
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductListed> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductListed> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Set an error handler
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, ProductListed> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        JsonDeserializer<ProductListed> deserializer = new JsonDeserializer<>(ProductListed.class);
        deserializer.addTrustedPackages("enit.ecomerce.search_product.consumer");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

   @Bean
public DefaultErrorHandler errorHandler() {
   
    FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2);

    DefaultErrorHandler errorHandler = new DefaultErrorHandler((record, exception) -> {
        try {
            // Convert the value to a String if possible
            String valueAsString = record.value() != null ? record.value().toString() : "null";

            // Log the error and send the record to the dead-letter topic
            logger.error("Failed to process record: {}", valueAsString, exception);
            kafkaTemplate.send("dead-letter-topic", valueAsString);
        } catch (Exception e) {
            logger.error("Failed to send record to dead-letter-topic: {}", e.getMessage(), e);
        }
    }, fixedBackOff);

    // Handle SerializationException directly
    errorHandler.addNotRetryableExceptions(SerializationException.class);

    return errorHandler;
}

}
