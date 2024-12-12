
package com.microservices.order_service.kafka;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.microservices.order_service.model.Item;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.OrderRepository;
import com.microservices.order_service.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import static com.microservices.order_service.domain.OrderStatus.CREATED;

@Service
public class CartConsumer {

    OrderRepository orderRepository;

    @KafkaListener(topics = "order-cart-topic", groupId = "cartReceiver")
    public void listen(String event){

        LocalDateTime receivedAt = LocalDateTime.now();

        Order order = new Order();

        JsonObject cartEvent = JsonParser.parseString(event).getAsJsonObject();
        String cartId = cartEvent.get("cartId").getAsString();
        JsonArray itemsArray = cartEvent.get("items").getAsJsonArray();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Item>>(){}.getType();
        List<Item> items = gson.fromJson(itemsArray, listType);
        int quantity = cartEvent.get("quantity").getAsInt();
        order.setOrderStatus(CREATED);
        order.setStockVerification(false);
        order.setDeliveryVerification(false);
        order.setPriceVerification(false);
        order.setPaymentVerification(false);
        order.setReceivedAt(receivedAt);
        orderRepository.save(order);
    }
}


