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
    private UUID orderId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime deliveryDate;

    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    private Address address;


    @ManyToOne
    @JoinColumn(name = "delivery_address_address_id")
    private Address deliveryAddress;

    // Ajouter addressId en tant que UUID pour utiliser explicitement la colonne
    @Column(name = "addressId", insertable = false, updatable = false)
    private UUID addressId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    // Constructeur sans paramètres
    public Shipment() {
        this.createdDate = LocalDateTime.now();
        this.deliveryDate = LocalDateTime.now().plusDays(2);
        this.status = DeliveryStatus.PENDING;
    }

    

    // Constructeur avec paramètres
    public Shipment(UUID orderId, Address deliveryAddress, LocalDateTime createdDate, LocalDateTime deliveryDate) {
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.addressId = deliveryAddress.getAddressId();  // Assurez-vous de récupérer l'ID de l'adresse
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.deliveryDate = deliveryDate != null ? deliveryDate : LocalDateTime.now().plusDays(2);
        this.status = DeliveryStatus.PENDING;
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
        this.addressId = deliveryAddress.getAddressId();  // Mettez à jour l'ID de l'adresse
    }

    public UUID getAddressId() {
        return addressId;
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
