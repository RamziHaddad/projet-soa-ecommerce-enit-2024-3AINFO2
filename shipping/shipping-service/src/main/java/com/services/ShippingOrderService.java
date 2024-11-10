package com.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.entities.ShippingOrder;

import java.util.ArrayList;

public class ShippingOrderService {
    private Map<Long, ShippingOrder> orders = new HashMap<>();
    private long currentId = 1;

    public ShippingOrder createOrder(ShippingOrder order) {
        order.setOrderId(currentId++);
        orders.put(order.getOrderId(), order);
        return order;
    }

    public ShippingOrder getOrderById(Long orderId) {
        return orders.get(orderId);
    }

    public List<ShippingOrder> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public ShippingOrder updateOrder(Long orderId, ShippingOrder order) {
        if (orders.containsKey(orderId)) {
            order.setOrderId(orderId);
            orders.put(orderId, order);
            return order;
        }
        return null;
    }

    public boolean deleteOrder(Long orderId) {
        return orders.remove(orderId) != null;
    }
}

