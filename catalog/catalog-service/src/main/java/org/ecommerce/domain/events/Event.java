package org.ecommerce.domain.events;
import java.time.LocalDateTime;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.ToString;

@JsonTypeInfo(
 use = JsonTypeInfo.Id.NAME,
 include = JsonTypeInfo.As.EXISTING_PROPERTY,
 property = "eventType",
 visible = true
)
@JsonSubTypes({
 @JsonSubTypes.Type(value = ProductListed.class, name = "ProductListed"),
 @JsonSubTypes.Type(value = ProductUpdated.class, name = "ProductUpdated"),
 @JsonSubTypes.Type(value = ProductAvailabilityEvent.class, name = "ProductAvailability"),
})

@Data
@ToString
public abstract class Event {
    protected UUID eventId = UUID.randomUUID();
    protected String eventType;
    protected String aggregateType;
    protected String aggregateId;
    protected LocalDateTime createdAt = LocalDateTime.now();
    public Event(){};
    public Event(String eventType, String aggregateType, String aggregateId) {
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    
    
}