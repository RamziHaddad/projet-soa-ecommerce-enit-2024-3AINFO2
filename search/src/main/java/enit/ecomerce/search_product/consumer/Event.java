package enit.ecomerce.search_product.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
 use = JsonTypeInfo.Id.NAME,
 include = JsonTypeInfo.As.EXISTING_PROPERTY,
 property = "eventType",
 visible = true
)
@JsonSubTypes({
 @JsonSubTypes.Type(value = ProductListed.class, name = "ProductListed")
})

public abstract class Event {
    protected String eventId ;
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
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }
    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    } 

    public  String getAggregateID(){
        return this.aggregateId;
    }

    
}