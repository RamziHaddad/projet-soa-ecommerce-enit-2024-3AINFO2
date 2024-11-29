package com.enit.pricing.InventoryKafkaEvent.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enit.pricing.InventoryKafkaEvent.dto.InventoryEvent;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryKafkaProducer kafkaProducer;

    @PostMapping("/sendInventoryEvent")
    public String sendEvent(@RequestBody InventoryEvent product) {
        String topicName = "product-added"; 
        kafkaProducer.sendInventoryEvent(product, topicName);
        return "Event sent to Kafka topic!";
    }
}