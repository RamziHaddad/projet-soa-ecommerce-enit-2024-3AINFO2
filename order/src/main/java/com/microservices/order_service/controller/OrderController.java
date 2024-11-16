package com.microservices.order_service.controller;

import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return ResponseEntity.ok("Order placed successfully");
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteOrder(@PathVariable("id") Long id){
        orderService.DeleteOrder(id);
        return ResponseEntity.ok("Order deleted");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> UpdateOrder(@PathVariable("id") Long id, @RequestBody OrderRequest orderRequest){
        orderService.UpdateOrder(id, orderRequest);
        return ResponseEntity.ok("Order updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id){
        Order order = orderService.GetOrderById(id);
        return ResponseEntity.ok(order);
    }
}

