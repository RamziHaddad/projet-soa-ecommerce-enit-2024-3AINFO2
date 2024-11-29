package com.enit.pricing.InventoryKafkaEvent.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.enit.pricing.InventoryKafkaEvent.dto.InventoryEvent;

@Service
public class  InventoryKafkaProducer {


        @Autowired
        private KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    	void sendInventoryEvent(InventoryEvent product, String topicName) {
			kafkaTemplate.send(topicName, product)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Unable to send message=[" + product + "] due to: " + ex.getMessage());
                } else {
                    System.out.println("Sent message=[" + product +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }
            });
	}
    
}

