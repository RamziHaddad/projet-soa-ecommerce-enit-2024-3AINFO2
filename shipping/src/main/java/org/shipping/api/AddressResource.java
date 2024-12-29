package org.shipping.api;

import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.shipping.dto.AddressDTO;
import org.shipping.dto.AddressUpdateDTO;
import org.shipping.model.Address;
import org.shipping.service.AddressService;

import java.util.List;
import java.util.UUID;

@Path("/addresses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressResource {

    @Inject
    AddressService addressService;

    private static final Logger logger = Logger.getLogger(AddressResource.class);

    // Ajouter une adresse pour un utilisateur
    @POST
    @Path("/add/{userId}")
    @Transactional
    public Response addAddress(@Valid AddressDTO addressDTO, @PathParam("userId") UUID userId) {
        try {
            Address createdAddress = addressService.addAddress(
                    userId,
                    addressDTO.getStreet(),
                    addressDTO.getPostalCode(),
                    addressDTO.getCity(),
                    addressDTO.getCountry());

            return Response.status(Response.Status.CREATED).entity(createdAddress).build();

        } catch (ConstraintViolationException e) {
            logger.warn("Validation error during address addition: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input during address addition: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            logger.warn("Address already exists: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error during address addition", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.")
                    .build();
        }
    }

    // Trouver les adresses de l'utilisateur courant
    @GET
    @Path("/{userId}")
    public Response getUserAddresses(UUID userId) {
        try {
            List<Address> addresses = addressService.getAddressesByUserId(userId);
            return Response.ok(addresses).build();

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input during address retrieval: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NoResultException e) {
            logger.warn("No addresses found for the user: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error during address retrieval", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.")
                    .build();
        }
    }

    // Récupérer une adresse par son ID
    @GET
    @Path("/{addressId}")
    public Response getAddressById(@PathParam("addressId") UUID addressId) {
        try {
            Address address = addressService.getAddressById(addressId);
            return Response.status(Response.Status.OK).entity(address).build();

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input during address retrieval by ID: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NoResultException e) {
            logger.warn("Address not found for ID " + addressId + ": " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error during address retrieval", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.")
                    .build();
        }
    }

    @PUT
    @Path("/{addressId}/update")
    @Transactional
    public Response updateAddress(@PathParam("addressId") UUID addressId, @Valid AddressUpdateDTO addressDTO) {
        try {
            Address updatedAddress = addressService.updateAddress(
                    addressId,
                    addressDTO.getStreet(),
                    addressDTO.getPostalCode(),
                    addressDTO.getCity(),
                    addressDTO.getCountry());
            return Response.status(Response.Status.OK).entity(updatedAddress).build();
        } catch (IllegalArgumentException | NoResultException e) {
            logger.warn("Error during address update: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error during address update", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.")
                    .build();
        }
    }

    // Supprimer une adresse
    @DELETE
    @Path("/{addressId}/delete")
    @Transactional
    public Response deleteAddress(@PathParam("addressId") UUID addressId) {
        try {
            addressService.deleteAddress(addressId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error during address deletion", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.")
                    .build();
        }
    }
}
