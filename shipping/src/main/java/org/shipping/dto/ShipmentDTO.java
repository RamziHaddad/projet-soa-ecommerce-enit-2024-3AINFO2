package org.shipping.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.shipping.model.Shipment.DeliveryStatus;

public class ShipmentDTO {

    private UUID shipmentId; // Identifiant unique de la livraison
    private UUID orderId; // Identifiant de la commande
    private LocalDateTime deliveryDate; // Date de livraison
    private UUID addressId; // Identifiant de l'adresse de livraison
    private DeliveryStatus status; // Statut de la livraison ( PENDING, IN_PROGRESS, DELIVERED )
    
    // Constructeur sans paramètres (nécessaire pour les frameworks comme Jackson)
    public ShipmentDTO() {
    }

    // Constructeur avec paramètres
    public ShipmentDTO(UUID shipmentId, UUID orderId, LocalDateTime deliveryDate, UUID addressId, DeliveryStatus status) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.deliveryDate = deliveryDate;
        this.addressId = addressId;
        this.status = status;
    }

    // Getters et setters
    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
