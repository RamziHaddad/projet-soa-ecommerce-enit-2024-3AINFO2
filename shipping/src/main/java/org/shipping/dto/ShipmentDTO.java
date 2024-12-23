package org.shipping.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public class ShipmentDTO {

    @NotNull(message = "Order ID cannot be null")
    private UUID orderId; // Identifiant de la commande

    @NotNull(message = "Address ID cannot be null")
    private UUID addressId; // Identifiant de l'adresse de livraison

    // Constructeur sans paramètres (nécessaire pour les frameworks comme Jackson)
    public ShipmentDTO() {
    }

    // Constructeur avec paramètres
    public ShipmentDTO(UUID orderId, UUID addressId) {
        this.orderId = orderId;
        this.addressId = addressId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }
}
