package com.enit.pricing.domain;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "products")

public class Product {
	
    @Id
    @Column(name = "product_id", updatable = true, nullable = false)
    private UUID productId;

    @Column(name = "base_price", precision = 38, scale = 4, nullable = true)
    private BigDecimal basePrice;

    @Column(name="current_price",precision = 38, scale = 4, nullable = true)
    private BigDecimal currentPrice;


    @ManyToOne
    @JoinColumn(name="promotion_id", nullable =true)
    ProductPromotion promotion;


	public Product(UUID productId) {
        this.productId = productId;
    }

    public Product() {
    }

    public Product(UUID productId, BigDecimal basePrice, String category, BigDecimal currentPrice) {
        this.productId = productId;
        this.basePrice = basePrice;
        this.currentPrice=currentPrice;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
    public ProductPromotion getPromotion() {
		return promotion;
	}

	public void setPromotion(ProductPromotion promotion) {
		this.promotion = promotion;
	}

	public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    
        public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
   



}