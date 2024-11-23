package org.ecommerce.model;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Product extends PanacheEntityBase{
    @Id
    @GeneratedValue
    private UUID id;
    private int totalQuantity;
    private int reservedQuantity;

    
    public Product() {
    }

    public Product(UUID id, int quantity) {
        this.id = id;
        this.totalQuantity = quantity;
        this.reservedQuantity = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public int availableQuantity() {
        return totalQuantity - reservedQuantity;
    }
}
