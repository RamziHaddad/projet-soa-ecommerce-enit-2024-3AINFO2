package com.enit.pricing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CatalogPriceResponse {
    private UUID productId;
    private BigDecimal price;

    public CatalogPriceResponse(UUID productId, BigDecimal price) {
        this.productId = productId;
        this.price = price;
    }
    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void Price(BigDecimal price) {
        this.price = price;
    }
}
