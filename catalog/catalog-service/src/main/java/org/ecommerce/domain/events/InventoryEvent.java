package org.ecommerce.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryEvent extends Event {
    @JsonProperty("productId")
    private String productId;
    private String name;
    private String description;
    private CategoryDTO category;
    private boolean disponibility;


    public InventoryEvent(String eventType, String productId, String name, String description, CategoryDTO category, boolean disponibility) {
        super(eventType, "Inventory", productId);
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.disponibility = disponibility;
    }
}
