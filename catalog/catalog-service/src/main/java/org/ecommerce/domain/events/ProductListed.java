package org.ecommerce.domain.events;

import org.ecommerce.domain.Product;

public class ProductListed extends Event {
    private final String productName;
    private final String categoryName;
    private final String description;
    private final double price;

    public ProductListed(Product product) {
        super("productListed", "Product", product.getId().toString());
        this.productName = product.getProductName();
        this.categoryName = product.getCategory().getCategoryName(); // Assuming `getName()` returns the category name
        this.description = product.getDescription();
        this.price = product.getShownPrice();
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
