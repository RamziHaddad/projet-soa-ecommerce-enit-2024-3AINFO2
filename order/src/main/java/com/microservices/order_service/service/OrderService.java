package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderRequest;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        // map OrderRequest to Order object
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setSkuCode(orderRequest.skuCode());
        order.setQuantity(orderRequest.quantity());
        // save order to OrderRepository
        orderRepository.save(order);
    }

    public void DeleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    public void UpdateOrder(Long id, OrderRequest orderRequest){
        Order order = orderRepository.findById(id).get();
        order.setPrice(orderRequest.price());
        order.setSkuCode(orderRequest.skuCode());
        order.setQuantity(orderRequest.quantity());
        orderRepository.save(order);
    }

    public Order GetOrderById(Long id){
        return orderRepository.findById(id).get();
    }
}
