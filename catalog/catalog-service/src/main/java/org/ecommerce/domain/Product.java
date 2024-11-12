package org.ecommerce.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {
    @Id
    private UUID id;
    private String productName;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    private ProductCategory category;
    private double basePrice;
    private double shownPrice=basePrice;
    public Product() {
    }
    
    

    public Product(String productName, String description, LocalDateTime createdAt, ProductCategory category,
            double basePrice, double shownPrice) {
        this.productName = productName;
        this.description = description;
        this.createdAt = createdAt;
        this.category = category;
        this.basePrice = basePrice;
        this.shownPrice = shownPrice;
    }



    public Product(UUID id, String productName, String description, LocalDateTime createdAt, ProductCategory category,
            double basePrice, double shownPrice) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.createdAt = createdAt;
        this.category = category;
        this.basePrice = basePrice;
        this.shownPrice = shownPrice;
    }



    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



    public ProductCategory getCategory() {
        return category;
    }



    public double getBasePrice() {
        return basePrice;
    }



    public double getShownPrice() {
        return shownPrice;
    }



    public void setCategory(ProductCategory category) {
        this.category = category;
    }



    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }



    public void setShownPrice(double shownPrice) {
        this.shownPrice = shownPrice;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        long temp;
        temp = Double.doubleToLongBits(basePrice);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(shownPrice);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (productName == null) {
            if (other.productName != null)
                return false;
        } else if (!productName.equals(other.productName))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (Double.doubleToLongBits(basePrice) != Double.doubleToLongBits(other.basePrice))
            return false;
        if (Double.doubleToLongBits(shownPrice) != Double.doubleToLongBits(other.shownPrice))
            return false;
        return true;
    }



    @Override
    public String toString() {
        return "Product [id=" + id + ", productName=" + productName + ", description=" + description + ", createdAt="
                + createdAt + ", category=" + category + ", basePrice=" + basePrice + ", shownPrice=" + shownPrice
                + "]";
    }
    

}
