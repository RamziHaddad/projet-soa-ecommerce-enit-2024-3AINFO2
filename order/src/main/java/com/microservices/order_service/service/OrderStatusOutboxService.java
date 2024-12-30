package com.microservices.order_service.service;


import com.microservices.order_service.dto.OrderStatusUpdateDTO;
import com.microservices.order_service.kafka.OrderStatusUpdateProducer;
import com.microservices.order_service.outbox.OrderStatusOutbox;
import com.microservices.order_service.repository.OrderStatusOutboxRepository;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusOutboxService {

    private static final Logger logger = Logger.getLogger(OrderStatusOutbox.class);

    @Autowired
    private OrderStatusOutboxRepository orderStatusOutboxRepository;

    @Autowired
    private OrderStatusUpdateProducer orderStatusUpdateProducer;

    @Scheduled(fixedRate = 30000)
    public void processOrderStatusOutbox() {

        List<OrderStatusOutbox> events = orderStatusOutboxRepository.findAll();

        for (OrderStatusOutbox event : events) {
            Boolean procssed = event.getProcessed();
            if(!procssed) {
                try {
                    OrderStatusUpdateDTO orderStatusUpdateDTO = new OrderStatusUpdateDTO();
                    orderStatusUpdateDTO.setOrderId(event.getOrderId());
                    orderStatusUpdateDTO.setStatus(event.getStatus());
                    orderStatusUpdateProducer.sendOrderEvent(orderStatusUpdateDTO);
                    event.setProcessed(true);
                    orderStatusOutboxRepository.save(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
