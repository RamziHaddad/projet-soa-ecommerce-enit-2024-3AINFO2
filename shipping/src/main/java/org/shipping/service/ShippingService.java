package org.shipping.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.shipping.messaging.DeliveryStatusPublisher;
import org.shipping.model.Address;
import org.shipping.model.Shipment;
import org.shipping.model.Shipment.DeliveryStatus;
import org.shipping.repository.ShipmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ShippingService {

    @Inject
    ShipmentRepository shipmentRepository;

    @Inject
    AddressService addressService;

    @Inject
    SecurityService securityService;

    @Inject
    DeliveryStatusPublisher statusPublisher;

    // Créer une nouvelle livraison
    @Transactional
    public Shipment createShipment(UUID orderId, UUID addressId) {
        Address address = addressService.getAddressById(addressId);
        Shipment shipment = new Shipment(orderId, address);
        shipmentRepository.persist(shipment);
        return shipment;
    }

    // Obtenir une livraison par ID
    public Shipment getShipmentById(UUID shipmentId) {
        return shipmentRepository.find("shipmentId", shipmentId).firstResult();
    }

    // Obtenir une livraison par ID
    public Shipment getShipmentByOrderId(UUID orderId) {
        return shipmentRepository.find("orderId", orderId).firstResult();
    }

    // Mettre à jour le statut d'une livraison
    @Transactional
    public Shipment updateShipment(UUID orderId, LocalDateTime deliveryDate, DeliveryStatus status) {
        Shipment shipment = getShipmentByOrderId(orderId);
        if (shipmentRepository.isPersistent(shipment)) {
            if (deliveryDate != null) {
                shipment.setDeliveryDate(deliveryDate);
            }
            if (status != null) {
                shipment.setStatus(status);
                statusPublisher.publishStatus(orderId, status);
            }
            shipmentRepository.persist(shipment);
        }
        return shipment;
    }

    // Récupérer les livraisons d'un utilisateur
    public List<Shipment> getUserShipmentHistory(DeliveryStatus status) {
        // UUID userId = securityService.getCurrentUserId();
        UUID userId = UUID.fromString("faa1b47d-27e3-4106-b42b-2d1e7d1f6e93");
        if (status != null) {
            // Si un statut est spécifié, filtrer les livraisons par utilisateur et statut
            return shipmentRepository.find("deliveryAddress.userId = ?1 AND status = ?2", userId, status).list();
        } else {
            // Sinon, récupérer toutes les livraisons de l'utilisateur
            return shipmentRepository.find("deliveryAddress.userId = ?1", userId).list();
        }
    }

    // Supprimer une livraison par ID
    @Transactional
    public void deleteShipment(UUID shipmentId) {
        Shipment shipment = shipmentRepository.find("shipmentId", shipmentId).firstResult();
        if (shipment != null) {
            shipmentRepository.delete(shipment);
        } else {
            throw new IllegalArgumentException("Shipment with ID " + shipmentId + " not found.");
        }
    }
}
