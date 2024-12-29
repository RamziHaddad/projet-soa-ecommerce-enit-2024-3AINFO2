package org.soa.model;

import java.util.UUID;

public class Item {

    private UUID itemId;
    private int quantity;
    private double price;
    private String name;
    private double totalPrice;

    public Item() {
    }

    public Item(UUID itemId, int quantity, double price, String name) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.totalPrice = price * quantity;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.price * quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.totalPrice = price * this.quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
