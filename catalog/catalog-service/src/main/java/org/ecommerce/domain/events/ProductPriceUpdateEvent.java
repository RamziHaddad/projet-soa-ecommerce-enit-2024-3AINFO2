package org.ecommerce.domain.events;


import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@ToString
public class ProductPriceUpdateEvent {
    private String productId;
    private BigDecimal price;

    // public ProductPriceUpdateEvent(UUID productId, BigDecimal price) {
    //     this.productId = productId;
    //     this.price = price;
    // }

}
