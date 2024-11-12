package com.enit.pricing.domain;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name="base_price", nullable = false)
    private double basePrice;

    @Column(name="category")
    private String category;

    @ManyToMany 
    @JoinTable(
        name= "product_promotions",
        joinColumns = @JoinColumn(name= "product_id"),
        inverseJoinColumns = @JoinColumn(name="promotion_id")
    )
    private Set<Promotion> promotions; 



    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}

