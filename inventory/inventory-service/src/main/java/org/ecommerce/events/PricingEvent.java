package org.ecommerce.events;

import java.util.UUID;

public class PricingEvent {
    private UUID productId;

    public PricingEvent(){}

    public PricingEvent(UUID id){
        this.productId=id;
    }
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
