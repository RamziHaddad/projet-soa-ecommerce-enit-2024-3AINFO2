package com.microservices.order_service.service;


import com.microservices.order_service.events.OrderPaidEvent;
import com.microservices.order_service.kafka.OrderPaidEventProducer;
import com.microservices.order_service.outbox.OrderPaidOutbox;
import com.microservices.order_service.repository.OrderPaidOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class OrderPaidOutboxService {

    private static final Logger logger = Logger.getLogger(OrderPaidOutboxService.class.getName());

    @Autowired
    private OrderPaidOutboxRepository orderPaidOutboxRepository;

    @Autowired
    private OrderPaidEventProducer orderPaidEventProducer;

    @Scheduled(fixedRate = 30000)
    public void processOrderPaidOutbox() {

        List<OrderPaidOutbox> events = orderPaidOutboxRepository.findAll();

        for (OrderPaidOutbox event : events) {
            Boolean processed = event.getProcessed();
            if (!processed) {
                try {
                    OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
                    orderPaidEvent.setOrderId(event.getOrderId());
                    orderPaidEvent.setAddressId(event.getAddressId());
                    orderPaidEvent.setCartId(event.getCartId());
                    orderPaidEvent.setPaymentStatus(event.getPaymentStatus());
                    orderPaidEvent.setPaymentStatus(event.getPaymentStatus());
                    orderPaidEventProducer.publishOrderPaidEvent(orderPaidEvent);
                    event.setProcessed(true);
                    orderPaidOutboxRepository.save(event);

                } catch (Exception e) {
                    logger.info("Outbox processing error: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }

    }
}
