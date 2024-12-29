
package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.CartDTO;
import com.microservices.order_service.dto.ItemDTO;
import com.microservices.order_service.model.Item;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.ItemRepository;
import com.microservices.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.microservices.order_service.domain.OrderStatus.CREATED;

@Service
public class CartConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CartConsumer.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

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
            item.setItemId(itemDTO.getId());
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
        orderRepository.save(order);
    }
}


