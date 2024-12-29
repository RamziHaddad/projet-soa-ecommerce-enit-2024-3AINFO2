package com.microservices.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.microservices.order_service.dto.OrderEventDTO;
import com.microservices.order_service.kafka.OrderEventProducer;
import com.microservices.order_service.outbox.OrderEventOutbox;
import com.microservices.order_service.repository.OrderEventOutboxRepository;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OrderEventOutboxService {

    private static final Logger logger = Logger.getLogger(OrderEventOutboxService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private OrderEventOutboxRepository orderEventOutboxRepository;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Scheduled(fixedRate = 60000)
    public void processOrderEventOutbox() {

        List<OrderEventOutbox> events = orderEventOutboxRepository.findAll();

        objectMapper.registerModule(new JavaTimeModule());

        for (OrderEventOutbox event : events) {
            Boolean processed = event.getProcessed();
            if (!processed) {
                try{

                    Gson gson = new Gson();

                    String payload = event.getPayload();
                    OrderEventDTO orderEventDTO = objectMapper.readValue(payload, OrderEventDTO.class);
                    orderEventProducer.sendOrderEvent(orderEventDTO);
                    event.setProcessed(true);
                    orderEventOutboxRepository.save(event);
                } catch (Exception e) {
                    logger.info("Outbox processing error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }
}
