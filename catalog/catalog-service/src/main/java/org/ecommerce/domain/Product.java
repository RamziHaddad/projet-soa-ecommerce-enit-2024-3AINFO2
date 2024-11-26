package org.ecommerce.domain;

import java.math.BigDecimal;
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
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;
    private BigDecimal basePrice;
    private BigDecimal shownPrice = basePrice;
    private boolean disponibility=false;

    public Product() {
    }

    public Product(String productName, String description, LocalDateTime createdAt, ProductCategory category,
                   BigDecimal basePrice, BigDecimal shownPrice, boolean disponibility) {
        this.productName = productName;
        this.description = description;
        this.createdAt = createdAt;
        this.category = category;
        this.basePrice = basePrice;
        this.shownPrice = shownPrice;
        this.disponibility = disponibility;
    }

    public Product(UUID id, String productName, String description, LocalDateTime createdAt, ProductCategory category,
                   BigDecimal basePrice, BigDecimal shownPrice, boolean disponibility) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.createdAt = createdAt;
        this.category = category;
        this.basePrice = basePrice;
        this.shownPrice = shownPrice;
        this.disponibility = disponibility;
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

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getShownPrice() {
        return shownPrice;
    }

    public void setShownPrice(BigDecimal shownPrice) {
        this.shownPrice = shownPrice;
    }

    public boolean isDisponibility() {
        return disponibility;
    }

    public void setDisponibility(boolean disponibility) {
        this.disponibility = disponibility;
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
        result = prime * result + (disponibility ? 1231 : 1237);
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
        if (basePrice != other.basePrice)
            return false;
        if (shownPrice != other.shownPrice)
            return false;
        if (disponibility != other.disponibility)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", productName=" + productName + ", description=" + description + ", createdAt="
                + createdAt + ", category=" + category + ", basePrice=" + basePrice + ", shownPrice=" + shownPrice
                + ", disponibility=" + disponibility + "]";
    }
}
