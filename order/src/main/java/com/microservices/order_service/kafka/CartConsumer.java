
package com.microservices.order_service.kafka;

import com.microservices.order_service.domain.PaymentStatus;
import com.microservices.order_service.dto.*;
import com.microservices.order_service.model.Item;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.outbox.OrderEventOutbox;
import com.microservices.order_service.outbox.OrderPaidOutbox;
import com.microservices.order_service.outbox.OrderStatusOutbox;
import com.microservices.order_service.repository.*;
import com.microservices.order_service.service.DeliveryService;
import com.microservices.order_service.service.InventoryService;
import com.microservices.order_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static com.microservices.order_service.domain.OrderStatus.*;

@Service
public class CartConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CartConsumer.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderEventOutboxRepository orderEventOutboxRepository;

    @Autowired
    OrderStatusOutboxRepository orderStatusOutboxRepository;

    @Autowired
    OrderPaidOutboxRepository orderPaidOutboxRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    DeliveryService deliveryService;

    @KafkaListener(topics = "cart-topic", groupId = "cartReceiver", containerFactory = "CartListenerContainerFactory")
    public void listen(CartDTO cartDTO) {

        logger.info("Received event with ID :"+cartDTO.getCartId());


        LocalDateTime receivedAt = LocalDateTime.now();

        Order order = new Order();
        order.setReceivedAt(receivedAt);

        List<ItemDTO> itemsDTO = cartDTO.getItems();

        List<Item> items = new ArrayList<>();

        for (ItemDTO itemDTO : itemsDTO) {
            Item item = new Item();
            item.setItemId(itemDTO.getItemId());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order);
            items.add(item);
        }

        order.setItems(items);



        UUID cartId = cartDTO.getCartId();
        order.setIdCart(cartId);
        order.setOrderStatus(CREATED);
        order.setStockVerification(false);
        order.setDeliveryVerification(false);
        order.setPriceVerification(false);
        order.setPaymentVerification(false);
        order.setReceivedAt(receivedAt);

        OrderEventOutbox orderEventOutbox = new OrderEventOutbox();

        OrderEventDTO orderEventDTO = new OrderEventDTO();

        orderEventDTO.setOrderStatus(CREATED.toString());
        orderEventDTO.setItems(items);
        orderEventOutbox.setProcessed(false);
        try{
            orderRepository.save(order);
            orderEventOutbox.setOrderId(order.getId());
            orderEventDTO.setOrderId(order.getId());
            orderEventOutbox.setPayload(orderEventDTO.convertToJson());



            AvailabilityCheckDTO availabilityCheckDTO = new AvailabilityCheckDTO();
            availabilityCheckDTO.setOrderId(order.getId());
            availabilityCheckDTO.setItems(items);

            Map<String, Object> AvailabilityCheckResponse = inventoryService.checkOrderAvailability(availabilityCheckDTO);
            if ("OK".equals(AvailabilityCheckResponse.get("status"))){
                orderEventOutboxRepository.save(orderEventOutbox);
            }
            else {
                OrderStatusOutbox orderStatusOutbox = new OrderStatusOutbox();
                orderStatusOutbox.setOrderId(order.getId());
                orderStatusOutbox.setStatus(CANCELED);
                orderStatusOutbox.setProcessed(false);
                orderStatusOutboxRepository.save(orderStatusOutbox);
                orderRepository.delete(order);
            }



        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error while saving order");
        }

    }
}


