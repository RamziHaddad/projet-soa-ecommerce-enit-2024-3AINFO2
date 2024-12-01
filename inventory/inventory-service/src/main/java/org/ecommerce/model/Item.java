package org.ecommerce.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class Item {
    @Id
    private UUID id;
    private int quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
