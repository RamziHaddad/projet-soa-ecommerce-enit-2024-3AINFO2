package com.enit.pricing.events.producer;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

import com.enit.pricing.events.dto.PriceUpdateEvent;


@Service
public class  PriceUpdateProducer {

    @Value("${spring.kafka.topic.price-update-topic}")
    private String productPrice;

        @Autowired
        private KafkaTemplate<String, PriceUpdateEvent> kafkaTemplate;
    	public void sendPrice(PriceUpdateEvent price) {
			kafkaTemplate.send(productPrice, price)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Unable to send message=[" + price + "] due to: " + ex.getMessage());
                } else {
                    System.out.println("Sent message=[" + price +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }
            });
	}
    
}

