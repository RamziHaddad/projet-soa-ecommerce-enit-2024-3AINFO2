package org.ecommerce.domain.events;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.ecommerce.domain.Product;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductListed extends Event {
    private  String productName;
    private  String categoryName;
    private  String description;
    private  BigDecimal price;

    public ProductListed(Product product) {
        super("ProductListed", "Product", product.getId().toString());
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

    public BigDecimal getPrice() {
        return price;
    }
}
