package org.shipping.api;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;
import org.shipping.dto.DeliveryStatusMessage;
import org.shipping.dto.ShipmentDTO;
import org.shipping.dto.ShipmentUpdateDTO;
import org.shipping.messaging.DeliveryStatusPublisher;
import org.shipping.model.DeliveryStatus;
import org.shipping.model.Shipment;
import org.shipping.service.ShippingService;

import java.util.List;
import java.util.UUID;

@Path("/shipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingResource {

    @Inject
    ShippingService shippingService;

    @Inject
    DeliveryStatusPublisher deliveryStatusPublisher;

    private static final Logger logger = Logger.getLogger(ShippingResource.class);

    // Créer une nouvelle livraison
    @POST
    @Path("/create")
    @Transactional
    public Response createShipment(@Valid ShipmentDTO shipmentDTO) {
        try {
            logger.info("Creating shipment with Order ID: " + shipmentDTO.getOrderId() + " and Address ID: "
                    + shipmentDTO.getAddressId());

            Shipment createdShipment = shippingService.createShipment(shipmentDTO.getOrderId(),
                    shipmentDTO.getAddressId());
            logger.info("Shipment created successfully with ID: " + createdShipment.getShipmentId());

            return Response.status(Status.CREATED).entity(createdShipment).build();
        } catch (IllegalStateException e) {
            // Commande déjà associée à une livraison
            logger.error("Order Already exists: " + e.getMessage());
            return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
        } catch (NoResultException e) {
            logger.error("NoResultException: " + e.getMessage());
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred during shipment creation: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating shipment").build();
        }
    }

    // Obtenir une livraison par ID
    @GET
    @Path("/order/{orderId}/get")
    public Response getShipmentByOrderId(@PathParam("orderId") UUID orderId) {
        try {
            logger.info("Fetching shipment for Order ID: " + orderId);

            Shipment shipment = shippingService.getShipmentByOrderId(orderId);
            logger.info("Shipment found for Order ID: " + orderId);

            return Response.ok(shipment).build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Order ID: " + orderId + " - " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Order ID: " + e.getMessage()).build();
        } catch (NoResultException e) {
            logger.warn("No shipment found for Order ID: " + orderId);
            return Response.status(Response.Status.NOT_FOUND).entity("No shipment found for Order ID: " + orderId)
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching shipment: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Mettre à jour le statut ou la date estimée d'une livraison
    @PUT
    @Path("/order/{orderId}/update")
    @Transactional
    public Response updateShipment(@PathParam("orderId") UUID orderId, @Valid ShipmentUpdateDTO shipmentDTO) {
        try {
            logger.info("Updating shipment for Order ID: " + orderId);

            Shipment shipment = shippingService.getShipmentByOrderId(orderId);
            if (shipment.getStatus() == DeliveryStatus.DELIVERED) {
                logger.error("Cannot update a delivered shipment");
                return Response.status(Response.Status.BAD_REQUEST).entity("Cannot update a delivered shipment").build();
            }

            Shipment updatedShipment = shippingService.updateShipment(
                    orderId,
                    shipmentDTO.getDeliveryDate(),
                    shipmentDTO.getStatus());
            logger.info("Shipment updated successfully for Order ID: " + orderId);

            return Response.status(Response.Status.OK).entity(updatedShipment).build();
        } catch (NoResultException e) {
            logger.error("NoResultException: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid data provided for updating shipment: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating shipment: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Suivi de l'état d'une livraison
    @GET
    @Path("/track/{orderId}")
    public Response trackShipment(@PathParam("orderId") UUID orderId) {
        try {
            logger.info("Tracking shipment for Order ID: " + orderId);

            Shipment shipment = shippingService.getShipmentByOrderId(orderId);
            logger.info("Shipment status retrieved for Order ID: " + orderId);

            return Response.ok(shipment.getStatus()).build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Order ID: " + orderId + " - " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Order ID: " + e.getMessage()).build();
        } catch (NoResultException e) {
            logger.warn("No shipment found for Order ID: " + orderId);
            return Response.status(Response.Status.NOT_FOUND).entity("No shipment found for Order ID: " + orderId)
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while tracking shipment: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Récupérer les livraisons d'un utilisateur
    @GET
    @Path("/user/history")
    public Response getUserShipmentHistory(@QueryParam("status") DeliveryStatus status) {
        try {
            logger.info("Fetching shipment history for user with status: " + status);

            List<Shipment> shipments = shippingService.getUserShipmentHistory(status);

            if (shipments.isEmpty()) {
                logger.warn("No shipments found for user with status: " + status);
                return Response.status(Response.Status.NOT_FOUND).entity("No shipments found for the given status").build();
            }

            return Response.ok(shipments).build();
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for user shipment history: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching user shipment history: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Supprimer une livraison par ID
    @DELETE
    @Path("/{shipmentId}/delete")
    @Transactional
    public Response deleteShipment(@PathParam("shipmentId") UUID shipmentId) {
        try {
            logger.info("Deleting shipment with ID: " + shipmentId);

            // Vérification de la livraison
            Shipment shipment = shippingService.getShipmentById(shipmentId);
            if (shipment == null) {
                logger.error("No shipment found with ID: " + shipmentId);
                return Response.status(Response.Status.NOT_FOUND).entity("Shipment not found").build();
            }

            // Vérification du statut
            if (shipment.getStatus() == DeliveryStatus.DELIVERED) {
                logger.error("Cannot delete a delivered shipment");
                return Response.status(Response.Status.BAD_REQUEST).entity("Cannot delete a delivered shipment").build();
            }

            shippingService.deleteShipment(shipmentId);
            logger.info("Shipment deleted successfully with ID: " + shipmentId);

            return Response.noContent().build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting shipment: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting shipment").build();
        }
    }

    @POST
    @Path("/delivery-status")
    public String updateDeliveryStatus(@Valid DeliveryStatusMessage deliveryStatusMessage) {
        deliveryStatusPublisher.publishStatus(deliveryStatusMessage.getOrderId(), deliveryStatusMessage.getStatus());
        return "Delivery Status Updated";


    }

}
