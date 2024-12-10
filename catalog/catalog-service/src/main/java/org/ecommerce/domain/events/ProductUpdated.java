package org.ecommerce.domain.events;

import org.ecommerce.domain.Product;

public class ProductUpdated extends Event {
    private final Product product;

    public ProductUpdated(Product product) {
        super("ProductUpdated", "Product", product.getId().toString());
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "ProductUpdated{" +
               "product=" + product +
               ", eventType=" + getEventType() +
               ", aggregateType=" + getAggregateType() +
               ", aggregateId=" + getAggregateId() +
               '}';
    }
}
