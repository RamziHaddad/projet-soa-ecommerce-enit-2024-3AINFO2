package org.ecommerce.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Represents an event related to inventory changes
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties during JSON deserialization
public class InventoryEvent extends Event {

    @JsonProperty("productId") // Maps the JSON property to the field
    private String productId; // Unique identifier for the product

    private String name; // Name of the product
    private String description; // Description of the product
    private CategoryDTO category; // Category information of the product
    private boolean disponibility; // Availability status of the product

    // Constructor for creating InventoryEvent with specified fields
    public InventoryEvent(String eventType, String productId, String name,
                          String description, CategoryDTO category, boolean disponibility) {
        super(eventType, "Inventory", productId); // Call the superclass constructor
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.disponibility = disponibility;
    }
}
