package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderDeliveryDTO;
import com.microservices.order_service.dto.OrderEventDTO;
import com.microservices.order_service.model.Client;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.ClientRepository;
import com.microservices.order_service.repository.OrderRepository;
import com.microservices.order_service.kafka.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final DeliveryService deliveryService;
    private final OrderEventProducer orderEventProducer;

    /**
     * Place a new order and persist it in the database.
     */
    public void placeOrder(Order order) {
        // Save the order in the database
        orderRepository.save(order);
    }

    /**
     * Process the order by initiating delivery after payment is successful.
     */
/*    public void processOrder(UUID clientId, UUID orderId, String clientAddress) {
        System.out.println("Payment successful for Order: " + orderId);

        // Trigger delivery
        OrderDeliveryDTO deliveryDTO = new OrderDeliveryDTO(clientId, orderId, clientAddress);
        deliveryService.startDelivery(deliveryDTO);

        System.out.println("Delivery triggered for Order: " + orderId);

        // Publish an event to indicate order processing completion
        OrderEventDTO orderEventDTO = new OrderEventDTO(orderId, null, "PROCESSING");
        orderEventProducer.sendOrderEvent(orderEventDTO);
    }*/

    /**
     * Delete an order by ID.
     */
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    /**
     * Update the details of an existing order.
     */
    public void updateOrder(UUID id, Order orderToUpdate) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update order fields
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

        // Update client
        Client client = clientRepository.findById(orderToUpdate.getClient().getIdClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        order.setClient(client);

        // Save updated order
        orderRepository.save(order);
    }

    /**
     * Retrieve an order by its ID.
     */
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Handle the event when an order is paid by sending an event to the appropriate topic.
     */
    public void handleOrderPaidEvent(UUID orderId) {
        // Fetch the order from the database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create the OrderEventDTO
        OrderEventDTO orderEventDTO = new OrderEventDTO(orderId, order.getItems(), "PAID");

        // Send the event
        orderEventProducer.sendOrderEvent(orderEventDTO);
    }
}
