package org.shipping.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.shipping.dto.ShipmentDTO;
import org.shipping.model.Shipment;
import org.shipping.model.Shipment.DeliveryStatus;
import org.shipping.service.ShippingService;

import java.util.List;
import java.util.UUID;

@Path("/shipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingResource {

    @Inject
    ShippingService shippingService;

    // Créer une nouvelle livraison
    @POST
    @Path("/create")
    @Transactional
    public Response createShipment(ShipmentDTO shipmentDTO) {
        Shipment createdShipment = shippingService.createShipment(shipmentDTO.getOrderId(), shipmentDTO.getAddressId());
        return Response.status(Response.Status.CREATED).entity(createdShipment).build();
    }

    // Obtenir une livraison par ID
    @GET
    @Path("/order/{orderId}/get")
    public Response getShipmentByOrderId(@PathParam("orderId") UUID orderId) {
        Shipment shipment = shippingService.getShipmentByOrderId(orderId);
        if (shipment != null) {
            return Response.ok(shipment).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Mettre à jour le statut ou la date estimée d'une livraison
    @PUT
    @Path("/order/{orderId}/update")
    @Transactional
    public Response updateShipment(@PathParam("orderId") UUID orderId, ShipmentDTO shipmentDTO) {
        Shipment updatedShipment = shippingService.updateShipment(
                orderId,
                shipmentDTO.getDeliveryDate(),
                shipmentDTO.getStatus());
        return Response.status(Response.Status.OK).entity(updatedShipment).build();
    }

    // Suivi de l'état d'une livraison
    @GET
    @Path("/track/{orderId}")
    public Response trackShipment(@PathParam("orderId") UUID orderId) {
        Shipment shipment = shippingService.getShipmentByOrderId(orderId);
        if (shipment != null) {
            return Response.ok(shipment.getStatus()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Récupérer les livraisons d'un utilisateur
    @GET
    @Path("/user/history")
    public Response getUserShipmentHistory(@QueryParam("status") DeliveryStatus status) {
        List<Shipment> shipments = shippingService.getUserShipmentHistory(status);
        if (shipments.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No shipments found").build();
        }
        return Response.ok(shipments).build();
    }

    @DELETE
    @Path("/{shipmentId}/delete")
    public Response deleteShipment(@PathParam("shipmentId") UUID shipmentId) {
        shippingService.deleteShipment(shipmentId);
        return Response.noContent().build();
    }

}
