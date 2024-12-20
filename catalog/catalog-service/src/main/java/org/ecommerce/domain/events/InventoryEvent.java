package org.ecommerce.domain.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InventoryEvent extends Event {
    private String productId;
    private String name;
    private String description;
    private String category;
    private boolean disponibility;

    public InventoryEvent(String aggregateType, String aggregateId, String productId, String name, String description, String category, boolean disponibility) {
        super("InventoryEvent", aggregateType, aggregateId);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.disponibility = disponibility;
    }
}
