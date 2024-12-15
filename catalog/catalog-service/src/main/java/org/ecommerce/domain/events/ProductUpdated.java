package org.ecommerce.domain.events;

import java.math.BigDecimal;

import org.ecommerce.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProductUpdated extends Event {
    private String productName;
    private String description;
    private String categoryName;
    private BigDecimal shownPrice;
    private boolean disponibility;

    public ProductUpdated(Product product) {
        super("ProductUpdated", "Product", product.getId().toString());
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.categoryName = product.getCategory().getCategoryName();
        this.shownPrice = product.getShownPrice();
        this.disponibility = product.isDisponibility();
    }

}
