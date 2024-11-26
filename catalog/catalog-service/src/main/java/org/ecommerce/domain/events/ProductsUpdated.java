package org.ecommerce.domain.events;

import org.ecommerce.domain.Product;

public class ProductsUpdated extends Event {
    private final Product product;

    public ProductsUpdated(Product product) {
        super("productsUpdated", "Product", product.getId().toString());
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "ProductsUpdated{" +
               "product=" + product +
               ", eventType=" + getEventType() +
               ", aggregateType=" + getAggregateType() +
               ", aggregateId=" + getAggregateId() +
               '}';
    }
}
