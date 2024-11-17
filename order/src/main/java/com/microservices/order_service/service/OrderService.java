package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.dto.ItemRequest;
import com.microservices.order_service.model.Client;
import com.microservices.order_service.model.Item;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.ClientRepository;
import com.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository; // For fetching associated Client

    public void placeOrder(OrderRequest orderRequest) {
        // Map OrderRequest to Order object
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString()); // Generate unique order number
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setOrderStatus(orderRequest.orderStatus());
        order.setCoupon(orderRequest.coupon());
        order.setSentToShipmentAt(orderRequest.sentToShipmentAt());
        order.setReceivedAt(orderRequest.receivedAt());
        order.setPaymentVerification(orderRequest.paymentVerification());
        order.setPriceVerification(orderRequest.priceVerification());
        order.setDeliveryVerification(orderRequest.deliveryVerification());
        order.setStockVerification(orderRequest.stockVerification());

        // Set Client from idClient in OrderRequest
        Client client = clientRepository.findById(orderRequest.idClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        order.setClient(client);


        // Map items from OrderRequest
        List<Item> items = orderRequest.items().stream().map(itemRequest -> {
            Item item = new Item();
            item.setId(itemRequest.getId());
            item.setPrice(itemRequest.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            return item;
        }).toList();
        order.setItems(items);

        // Save order to the database
        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public void updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update fields from OrderRequest
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setOrderStatus(orderRequest.orderStatus());
        order.setCoupon(orderRequest.coupon());
        order.setSentToShipmentAt(orderRequest.sentToShipmentAt());
        order.setReceivedAt(orderRequest.receivedAt());
        order.setPaymentVerification(orderRequest.paymentVerification());
        order.setPriceVerification(orderRequest.priceVerification());
        order.setDeliveryVerification(orderRequest.deliveryVerification());
        order.setStockVerification(orderRequest.stockVerification());

        // Update items
        List<Item> items = orderRequest.items().stream().map(itemRequest -> {
            Item item = new Item();
            item.setId(itemRequest.getId());
            item.setPrice(itemRequest.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            return item;
        }).toList();
        order.setItems(items);

        // Update Client
        Client client = clientRepository.findById(orderRequest.idClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        order.setClient(client);

        // Save updated order to the database
        orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
