package org.shipping.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;
import org.shipping.messaging.DeliveryStatusPublisher;
import org.shipping.model.Address;
import org.shipping.model.DeliveryStatus;
import org.shipping.model.Shipment;
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
                throw new NoResultException("Address not found for ID: " + addressId);
            }
            
            // Vérification si la commande est déjà associée à une livraison
            Shipment shipment = shipmentRepository.find("orderId", orderId).firstResult();
            if (shipment != null) {
                throw new IllegalStateException("This order is already associated to a shipment.");
            }

            // Créer et persister la livraison
            Shipment newShipment = new Shipment(orderId, address);
            shipmentRepository.persist(newShipment);
            statusPublisher.publishStatus(orderId, DeliveryStatus.PENDING); // Publier le statut de la livraison
            logger.info("Shipment created successfully: " + newShipment.getShipmentId());
            return newShipment;

        } catch (NoResultException e) {
            // Adresse non trouvée
            logger.error("NoResultException: " + e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            // Commande déjà associée à une livraison
            logger.error("Order Already exists: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Autres exceptions possibles
            logger.error("Unexpected error occurred while creating shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while creating shipment.", e);
        }
    }

    // Obtenir une livraison par OrderId
    public Shipment getShipmentByOrderId(UUID orderId) {
        try {
            logger.info("Fetching shipment for orderId: " + orderId);

            // Vérifier si l'orderId est valide
            if (orderId == null) {
                logger.error("Order ID cannot be null.");
                throw new IllegalArgumentException("Order ID cannot be null.");
            }

            // Rechercher la livraison par orderId
            Shipment shipment = shipmentRepository.find("orderId", orderId).firstResult();
            if (shipment == null) {
                logger.warn("No shipment found for order ID: " + orderId);
                throw new NoResultException("No shipment found for order ID: " + orderId);
            }

            return shipment;

        } catch (IllegalArgumentException e) {
            // Erreur de validation des arguments
            logger.error("IllegalArgumentException: " + e.getMessage());
            throw e;
        } catch (NoResultException e) {
            // Livraison non trouvée
            logger.error("NoResultException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Autres erreurs
            logger.error("Unexpected error occurred while retrieving shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while retrieving shipment.", e);
        }
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
                logger.info("Shipment updated successfully: " + shipment.getShipmentId());
            }

            return shipment;

        } catch (NoResultException e) {
            // Livraison non trouvée
            logger.error("NoResultException: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            // Erreur de validation des données
            logger.error("IllegalArgumentException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Erreur imprévue
            logger.error("Unexpected error occurred while updating shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while updating the shipment.", e);
        }
    }

    // Récupérer les livraisons d'un utilisateur
    public List<Shipment> getUserShipmentHistory(DeliveryStatus status) {
        try {
            // UUID userId = securityService.getCurrentUserId();
            UUID userId = UUID.fromString("faa1b47d-27e3-4106-b42b-2d1e7d1f6e93");

            logger.info("Fetching shipment history for userId: " + userId + " with status: " + status);

            // Si un statut est spécifié, filtrer les livraisons par utilisateur et statut
            if (status != null) {
                return shipmentRepository.find("deliveryAddress.userId = ?1 AND status = ?2", userId, status).list();
            } else {
                // Sinon, récupérer toutes les livraisons de l'utilisateur
                return shipmentRepository.find("deliveryAddress.userId = ?1", userId).list();
            }
        } catch (IllegalArgumentException e) {
            // Erreur de validation des données
            logger.error("IllegalArgumentException: " + e.getMessage());
            throw new IllegalArgumentException("Invalid user ID format: " + e.getMessage());
        } catch (Exception e) {
            // Autres erreurs
            logger.error("Unexpected error occurred while retrieving user shipment history: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while retrieving user shipment history.", e);
        }
    }

    // Supprimer une livraison par ID
    @Transactional
    public void deleteShipment(UUID shipmentId) {
        try {
            logger.info("Deleting shipment with shipmentId: " + shipmentId);

            // Rechercher la livraison par ID
            Shipment shipment = shipmentRepository.find("shipmentId", shipmentId).firstResult();

            // Vérifier si la livraison existe
            if (shipment != null) {
                shipmentRepository.delete(shipment);
                logger.info("Shipment deleted successfully: " + shipmentId);
            } else {
                // Si la livraison n'existe pas, lancer une exception
                logger.error("Shipment not found for ID: " + shipmentId);
                throw new NoResultException("Shipment with ID " + shipmentId + " not found.");
            }
        } catch (NoResultException e) {
            // Livraison non trouvée
            logger.error("NoResultException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Autres erreurs
            logger.error("Unexpected error occurred while deleting shipment: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while deleting shipment.", e);
        }
    }

}