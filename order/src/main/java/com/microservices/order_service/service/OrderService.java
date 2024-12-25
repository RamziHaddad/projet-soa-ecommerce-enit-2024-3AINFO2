package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderDeliveryDTO;
import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.dto.ItemRequest;
import com.microservices.order_service.model.Client;
import com.microservices.order_service.model.Item;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.ClientRepository;
import com.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final DeliveryService deliveryService;

    public void placeOrder(Order order) {
        // Map OrderRequest to Order object

        // Set Client from idClient in OrderRequest
        // Save order to the database
        orderRepository.save(order);
    }

    public void processOrder(UUID clientId, UUID orderId, String clientAddress) {
        // Simulate payment success
        System.out.println("Payment successful for Order: " + orderId);

        // Create OrderDeliveryDTO
        OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO(clientId, orderId, clientAddress);

        // Trigger Delivery
        deliveryService.startDelivery(deliveryDTO);
        System.out.println("Delivery triggered for Order: " + orderId);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    public void updateOrder(UUID id, Order orderToUpdate) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update fields from OrderRequest
        order.setPrice(orderToUpdate.getPrice());
        order.setQuantity(orderToUpdate.getQuantity());
        order.setOrderStatus(orderToUpdate.getOrderStatus());
        order.setCoupon(orderToUpdate.getCoupon());
        order.setSentToShipmentAt(orderToUpdate.getSentToShipmentAt());
        order.setReceivedAt(orderToUpdate.getReceivedAt());
        order.setPaymentVerification(orderToUpdate.getPaymentVerification());
        order.setPriceVerification(orderToUpdate.getPriceVerification());
        order.setDeliveryVerification(orderToUpdate.getDeliveryVerification());
        order.setStockVerification(orderToUpdate.getStockVerification());
        order.setItems(orderToUpdate.getItems());

        // Update Client
        Client client = clientRepository.findById(orderToUpdate.getClient().getIdClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        order.setClient(client);

        // Save updated order to the database
        orderRepository.save(order);
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
