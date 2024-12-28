package org.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class OrderEventDTO {
    @Id
    private UUID orderId;
    @OneToMany
    @JoinColumn(name="orderId")
    private List<Item> items;
    private String orderStatus;

    public OrderEventDTO(){}

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String toString(){
        String printed= "orderId: "+orderId+",  status:"+orderStatus+", items:"+items;
        printed+=items.stream()
                .map(Item::toString).reduce("",(partialResult,str)->partialResult+str);
    return printed;
    }
}
