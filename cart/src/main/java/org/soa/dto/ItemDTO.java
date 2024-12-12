package org.soa.dto;

import java.util.UUID;

public class ItemDTO {

    private UUID itemId; // Identifiant de l'item
    private String name; // Nom de l'item
    private int quantity;
    private double price; // Quantité de l'item

    // Constructeur sans paramètres
    public ItemDTO() {
    }

    // Constructeur avec paramètres
    public ItemDTO(UUID itemId, String name, int quantity, double price) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    
}
