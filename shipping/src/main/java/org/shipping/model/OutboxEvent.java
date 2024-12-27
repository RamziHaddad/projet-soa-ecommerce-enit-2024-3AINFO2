package org.shipping.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class OutboxEvent {

    @Id
    private UUID id;
    private UUID aggregateId;
    private String eventType;
    private String payload; 
    private String processed ;
    
    public OutboxEvent() {
    }

    public OutboxEvent(UUID id, UUID aggregateId, String eventType, String payload, String processed) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.processed = processed;
    }
    
    public OutboxEvent(UUID aggregateId, String eventType, String payload, String processed) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.processed = processed;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getAggregateId() {
        return aggregateId;
    }
    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public String isProcessed() {
        return processed;
    }
    public void setProcessed(String processed) {
        this.processed = processed;
    }

    
}
