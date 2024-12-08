package enit.ecomerce.search_product.emitter;

import java.util.Base64;
import java.util.HashMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import java.util.Map;

@Component
public class EventsEmitter {

    private static final Logger logger = LoggerFactory.getLogger(EventsEmitter.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void emitToDeadLetterTopic(
        String topic,
        int partition,
        long offset,
        Object key,
        byte[] rawValue,
        String errorMessage,
        String errorType
) {
    try {
        // Create the payload map
        Map<String, Object> deadLetterPayload = new HashMap<>();
        deadLetterPayload.put("originalTopic", topic);
        deadLetterPayload.put("partition", partition);
        deadLetterPayload.put("offset", offset);
        deadLetterPayload.put("key", key);
        deadLetterPayload.put("rawValue", rawValue);  
        deadLetterPayload.put("errorMessage", errorMessage);
        deadLetterPayload.put("errorType", errorType);
        deadLetterPayload.put("timestamp", System.currentTimeMillis());

     
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadJson = objectMapper.writeValueAsString(deadLetterPayload);
 
        kafkaTemplate.send("dead-letter-topic", key != null ? key.toString() : null, payloadJson);

        logger.info("Sent record to dead-letter topic: " + payloadJson);
    } catch (Exception e) {
        logger.error("Failed to send record to dead-letter topic", e);
    }
}
}
    

