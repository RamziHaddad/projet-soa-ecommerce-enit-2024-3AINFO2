package org.shipping.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.shipping.model.DeliveryStatus;
import org.shipping.validation.ValidDeliveryStatus;

import jakarta.validation.constraints.Future;

public class ShipmentUpdateDTO {

    @Future(message = "Delivery date must be in the future")
    private LocalDateTime deliveryDate; // Date de livraison

    private UUID addressId; // Identifiant de l'adresse de livraison

    @ValidDeliveryStatus(message = "Invalid delivery status") // Validation personnalisée
    private DeliveryStatus status;

    // Constructeur sans paramètres (nécessaire pour le framework Jackson)
    public ShipmentUpdateDTO() {
    }

    // Constructeur avec paramètres
    public ShipmentUpdateDTO(LocalDateTime deliveryDate, UUID addressId, DeliveryStatus status) {
        this.deliveryDate = deliveryDate;
        this.addressId = addressId;
        this.status = status;
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