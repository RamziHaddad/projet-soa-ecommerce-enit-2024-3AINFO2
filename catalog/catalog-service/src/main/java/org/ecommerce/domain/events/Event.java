package org.ecommerce.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.ToString;

// Base class representing an event in the event-driven architecture
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Use the name of the type as the type identifier
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // Include the existing property
        property = "eventType", // The property that contains the event type
        visible = true // Make the property visible in the serialized JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductListed.class, name = "ProductListed"),
        @JsonSubTypes.Type(value = ProductUpdated.class, name = "ProductUpdated"),
        @JsonSubTypes.Type(value = InventoryEvent.class, name = "CREATE"),
        @JsonSubTypes.Type(value = InventoryEvent.class, name = "UPDATE"),
        @JsonSubTypes.Type(value = InventoryEvent.class, name = "DELETE")
})

@Data
@ToString
public abstract class Event {
    protected UUID eventId = UUID.randomUUID(); // Unique identifier for the event
    protected String eventType; // Type of the event
    protected String aggregateType; // Type of the aggregate that the event pertains to
    protected String aggregateId; // Identifier of the aggregate that the event pertains to
    protected LocalDateTime createdAt = LocalDateTime.now(); // Timestamp of when the event was created

    // Default constructor
    public Event() {}

    // Constructor to initialize eventType, aggregateType, and aggregateId
    public Event(String eventType, String aggregateType, String aggregateId) {
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }

    // Getters
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object obj) {
        // Custom equality check based on eventId
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        return eventId != null ? eventId.equals(other.eventId) : other.eventId == null;
    }

    @Override
    public int hashCode() {
        // Custom hash code based on eventId
        return eventId != null ? eventId.hashCode() : 0;
    }
}
