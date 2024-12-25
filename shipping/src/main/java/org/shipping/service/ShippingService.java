package org.shipping.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.shipping.exception.AddressNotFoundException;
import org.shipping.exception.OrderAlreadyAssociatedException;
import org.shipping.messaging.DeliveryStatusPublisher;
import org.shipping.model.Address;
import org.shipping.model.DeliveryStatus;
import org.shipping.model.EventTypeMapper;
import org.shipping.model.Shipment;
import org.shipping.model.OutboxEvent;
import org.shipping.repository.ShipmentRepository;
import org.shipping.repository.OutboxRepository;

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

    @Inject
    OutboxRepository outboxRepository;

    private static final Logger logger = Logger.getLogger(ShippingService.class);

    // Créer une nouvelle livraison
    @Transactional
    public Shipment createShipment(UUID orderId, UUID addressId) {
        try {
            logger.info("Creating shipment for orderId: " + orderId + " with addressId: " + addressId);

            // Vérification si l'adresse existe
            Address address = addressService.getAddressById(addressId);
            if (address == null) {
                logger.error("Address not found for ID: " + addressId);
                throw new AddressNotFoundException("Address not found for ID: " + addressId);
            }

            // Vérification si la commande est déjà associée à une livraison
            Shipment shipment = shipmentRepository.find("orderId", orderId).firstResult();
            if (shipment != null) {
                logger.error("Order already associated with shipment: " + orderId);
                throw new OrderAlreadyAssociatedException("This order is already associated to a shipment.");
            }

            // Créer et persister la livraison
            Shipment newShipment = new Shipment(orderId, address, null, null);
            shipmentRepository.persist(newShipment);

            // Créer un événement et l'enregistrer dans la table outbox
            OutboxEvent outboxEvent = new OutboxEvent(
                UUID.randomUUID(),  // ID unique pour l'événement
                newShipment.getShipmentId(),  // ID de l'expédition comme AggregateId
                EventTypeMapper.getEventType("SHIPPING_CREATED"),  // Type d'événement générique (string)
                "{\"orderId\":\"" + orderId + "\", \"addressId\":\"" + addressId + "\"}",  // Payload en format JSON
                "false"  // Indicateur de traitement de l'événement
            );
            outboxRepository.persist(outboxEvent);

            // Publier le statut de la livraison
            statusPublisher.publishStatus(orderId, DeliveryStatus.PENDING);
            logger.info("Shipment created successfully: " + newShipment.getShipmentId());
            return newShipment;

        } catch (AddressNotFoundException e) {
            logger.error("Address not found: " + e.getMessage());
            throw e;
        } catch (OrderAlreadyAssociatedException e) {
            logger.error("Order already associated with shipment: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while creating shipment.", e);
        }
    }

    // Récupérer une livraison par orderId
    public Shipment getShipmentByOrderId(UUID orderId) {
        Shipment shipment = shipmentRepository.find("orderId", orderId).firstResult();
        if (shipment == null) {
            logger.error("No shipment found for orderId: " + orderId);
            throw new NoResultException("No shipment found for orderId: " + orderId);
        }
        return shipment;
    }

    // Ajouter la méthode getShipmentById pour récupérer une livraison par son ID
    public Shipment getShipmentById(UUID shipmentId) {
        Shipment shipment = shipmentRepository.find("shipmentId", shipmentId).firstResult();
        if (shipment == null) {
            logger.error("No shipment found for shipmentId: " + shipmentId);
            throw new NoResultException("No shipment found for shipmentId: " + shipmentId);
        }
        return shipment;
    }

    @Transactional
    public void deleteShipment(UUID shipmentId) {
        try {
            // Find the shipment by ID
            Shipment shipment = shipmentRepository.find("shipmentId", shipmentId).firstResult();
            if (shipment != null) {
                // Delete the shipment if it exists
                shipmentRepository.delete(shipment);
                logger.info("Shipment deleted successfully with ID: " + shipmentId);
            } else {
                logger.warn("Shipment with ID: " + shipmentId + " not found.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting shipment with ID: " + shipmentId, e);
            throw new RuntimeException("Error occurred while deleting shipment", e);
        }
    }

    public List<Shipment> getUserShipmentHistory(DeliveryStatus status) {
        // Logique pour récupérer l'historique des livraisons selon le statut
        return shipmentRepository.find("status", status).list();
    }

    // Mettre à jour le statut d'une livraison
    @Transactional
    public Shipment updateShipment(UUID orderId, LocalDateTime deliveryDate, DeliveryStatus status) {
        try {
            logger.info("Updating shipment for orderId: " + orderId);

            // Récupérer la livraison par orderId
            Shipment shipment = getShipmentByOrderId(orderId);

            // Vérifier la persistance avant de modifier
            if (shipmentRepository.isPersistent(shipment)) {
                if (deliveryDate != null) {
                    // Vérifier que la date de livraison n'est pas dans le passé
                    if (deliveryDate.isBefore(LocalDateTime.now())) {
                        logger.error("Delivery date cannot be in the past.");
                        throw new IllegalArgumentException("Delivery date cannot be in the past.");
                    }
                    shipment.setDeliveryDate(deliveryDate);
                }

                if (status != null) {
                    shipment.setStatus(status);
                    statusPublisher.publishStatus(orderId, status);
                }
                shipmentRepository.persist(shipment);

                // Récupérer l'ID de l'adresse associée à l'expédition
                UUID addressId = shipment.getDeliveryAddress().getAddressId();

                // Créer un événement et l'enregistrer dans la table outbox
                OutboxEvent outboxEvent = new OutboxEvent(
                    UUID.randomUUID(),  // ID unique pour l'événement
                    shipment.getShipmentId(),  // ID de l'expédition comme AggregateId
                    EventTypeMapper.getEventType("SHIPPING_UPDATED"),  // Type d'événement générique (string)
                    "{\"orderId\":\"" + orderId + "\", \"addressId\":\"" + addressId + "\"}",  // Payload en format JSON
                    "false"  // Indicateur de traitement de l'événement
                );
                outboxRepository.persist(outboxEvent);

                logger.info("Shipment updated successfully: " + shipment.getShipmentId());
            }

            return shipment;

        } catch (NoResultException e) {
            logger.error("NoResultException: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while updating the shipment.", e);
        }
    }
}
