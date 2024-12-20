package org.ecommerce.domain.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryEvent extends Event {

    private String productId;
    private String name;
    private String description;
    private String category;
    private boolean disponibility;

    public InventoryEvent(String eventType, String aggregateType, String aggregateId, String productId, String name, String description, String category, boolean disponibility) {
        super(eventType, aggregateType, aggregateId);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.disponibility = disponibility;
    }
}
