package com.microservices.order_service.controller;

import com.microservices.order_service.dto.AvailabilityCheckDTO;
import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.service.InventoryService;
import com.microservices.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final InventoryService inventoryService;

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

    @PostMapping("/placeOrder")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody AvailabilityCheckDTO availabilityCheckDTO) {
        Map<String, Object> response = inventoryService.checkOrderAvailability(availabilityCheckDTO);

        if ("ok".equals(response.get("status"))) {
            Order order = orderService.getOrderById(availabilityCheckDTO.getOrderId());
            order.setStockVerification(true);
            orderService.updateOrder(availabilityCheckDTO.getOrderId(), order);
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
}

