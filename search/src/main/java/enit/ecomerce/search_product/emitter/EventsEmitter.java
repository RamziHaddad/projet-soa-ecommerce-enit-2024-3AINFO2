package enit.ecomerce.search_product.emitter;

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

    public void sendToDeadLetterTopic(String sourceTopic, ConsumerRecord<String, Object> record, String errorMessage) {
        String deadLetterTopic = "dead-letter-topic";

        try {
            Map<String, Object> deadLetterEvent = new HashMap<>();
            deadLetterEvent.put("sourceTopic", sourceTopic);
            deadLetterEvent.put("partition", record.partition());
            deadLetterEvent.put("offset", record.offset());
            deadLetterEvent.put("key", record.key());
            deadLetterEvent.put("value", record.value());
            deadLetterEvent.put("error", errorMessage);

             
            String deadLetterJson = objectMapper.writeValueAsString(deadLetterEvent);

            
            kafkaTemplate.send(deadLetterTopic, record.key(), deadLetterJson);
            logger.info("Malformed event sent to Dead Letter Topic: {}", deadLetterJson);

        } catch (Exception e) {
            logger.error("Failed to send malformed event to Dead Letter Topic: {}", e.getMessage(), e);
        }
    }
}
    

