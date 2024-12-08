package com.microservices.order_service.controller;

import com.microservices.order_service.dto.*;
import com.microservices.order_service.kafka.OrderCreationProducer;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.service.InventoryService;
import com.microservices.order_service.service.OrderService;
import com.microservices.order_service.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final InventoryService inventoryService;

    private final OrderCreationProducer orderCreationProducer;

    private final PricingService pricingService;

    @PostMapping("/create")
    public ResponseEntity<String> placeOrder(@RequestBody Order order){

        orderService.placeOrder(order);
        return ResponseEntity.ok("Order placed successfully");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteOrder(@PathVariable("id") UUID id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> UpdateOrder(@PathVariable("id") UUID id, @RequestBody Order order){
        orderService.updateOrder(id, order);
        return ResponseEntity.ok("Order updated successfully");
    }

    @PostMapping("/sendOrder")
    public ResponseEntity<Map<String, Object>> sendOrderToInventory(@RequestBody AvailabilityCheckDTO availabilityCheckDTO) {
        Map<String, Object> response = inventoryService.checkOrderAvailability(availabilityCheckDTO);

        if ("ok".equals(response.get("status"))) {
            Order order = orderService.getOrderById(availabilityCheckDTO.getOrderId());
            order.setStockVerification(true);
            order.setOrderStatus("Order Created");
            orderService.updateOrder(availabilityCheckDTO.getOrderId(), order);

            OrderEventDTO orderEventDTO = new OrderEventDTO();
            orderEventDTO.setOrderId(order.getId());
            orderEventDTO.setItems(order.getItems());
            orderEventDTO.setOrderStatus("Order Created");
            orderCreationProducer.sendMessage(orderEventDTO);

            return ResponseEntity.ok(Map.of("message", "Order placed successfully"));
        } else {
            // we have to communicate with cart microservice
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Some items are out of stock", "details", response));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") UUID id){
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishOrder(@RequestBody OrderEventDTO orderEventDTO){
        orderCreationProducer.sendMessage(orderEventDTO);
        return ResponseEntity.ok("Order published");
    }

    @PostMapping("/checkOrder")
    public ResponseEntity<Map<String, Object>> CheckOrder(@RequestBody AvailabilityCheckDTO availabilityCheckDTO){
        Map<String, Object> response = inventoryService.checkOrderAvailability(availabilityCheckDTO);
        return ResponseEntity.ok(response);


    }
    @PostMapping("/checkPrice")
    public ResponseEntity<cartResponse> CheckPrice(@RequestBody List<CartItem> cartItems){
        cartResponse response = pricingService.checkPrice(cartItems);
        return ResponseEntity.ok(response);
    }
}