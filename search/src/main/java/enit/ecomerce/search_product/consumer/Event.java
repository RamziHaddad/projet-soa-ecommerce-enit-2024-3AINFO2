package enit.ecomerce.search_product.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Event {
    protected UUID eventId ;
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

    public  UUID getEventId(){
        return this.eventId;
    }

    
    
}