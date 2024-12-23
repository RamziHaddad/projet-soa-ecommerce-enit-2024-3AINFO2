package org.ecommerce.domain;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@ToString
public class OutboxEvent {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String aggregateType;

    private String aggregateId;
    
    @Column(columnDefinition="TEXT")
    private String message;
    

    public OutboxEvent() {
    }
    
    
    public OutboxEvent(String eventType, String status, LocalDateTime createdAt, String aggregateType,
            String aggregateId, String message) {
        this.eventType = eventType;
        this.status = status;
        this.createdAt = createdAt;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.message = message;
    }


    public OutboxEvent(UUID id, String eventType, String status, LocalDateTime createdAt, String aggregateType,
            String aggregateId, String message) {
        this.id = id;
        this.eventType = eventType;
        this.status = status;
        this.createdAt = createdAt;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.message = message;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
