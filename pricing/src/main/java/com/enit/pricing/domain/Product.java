package com.enit.pricing.domain;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")


public class Product {
	

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "product_id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID productId;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "category")
    private String category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, targetEntity = ProductPromotion.class)
    private Set<Promotion> promotions;



	public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Set<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    

   



}