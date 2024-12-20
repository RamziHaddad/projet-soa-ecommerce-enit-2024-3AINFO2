package org.shipping.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "orderId" }) })
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shipmentId;

    @Column(nullable = false, updatable = false)
    private UUID orderId; // Identifiant de la commande associée

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now(); // Date de création

    private LocalDateTime deliveryDate = LocalDateTime.now().plusDays(2); // Date de livraison prévue/effective

    @ManyToOne(optional = false)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId", nullable = false)
    private Address deliveryAddress; // Relation avec l’entité Address

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING; // Statut de la livraison

    // Constructeur sans paramètres
    public Shipment() {
    }

    // Constructeur avec paramètres
    public Shipment(UUID orderId, Address deliveryAddress) {
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters et setters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
