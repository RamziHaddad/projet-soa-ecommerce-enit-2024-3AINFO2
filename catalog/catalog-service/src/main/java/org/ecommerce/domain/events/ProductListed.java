package org.ecommerce.domain.events;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.ecommerce.domain.Product;

// Class representing a product listed event
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProductListed extends Event {
    private String productName; // Name of the product
    private String categoryName; // Name of the category
    private String description; // Description of the product
    private BigDecimal price; // Price of the product

    // Constructor to initialize ProductListed from a Product object
    public ProductListed(Product product) {
        super("ProductListed", "Product", product.getId().toString());
        this.productName = product.getProductName();
        this.categoryName = product.getCategory().getCategoryName();
        this.description = product.getDescription();
        this.price = product.getShownPrice();
    }
}
