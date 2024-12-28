package org.ecommerce.domain.events;

import java.math.BigDecimal;

import org.ecommerce.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Class representing a product updated event
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProductUpdated extends Event {
    private String productName;    // Name of the product
    private String description;     // Description of the product
    private String categoryName;    // Name of the product's category
    private BigDecimal shownPrice;  // Price displayed for the product
    private boolean disponibility;   // Availability of the product

    // Constructor to initialize ProductUpdated from a Product object
    public ProductUpdated(Product product) {
        super("ProductUpdated", "Product", product.getId().toString());
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.categoryName = product.getCategory().getCategoryName();
        this.shownPrice = product.getShownPrice();
        this.disponibility = product.isDisponibility();
    }
}
