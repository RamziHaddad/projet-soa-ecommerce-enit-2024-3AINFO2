package org.shipping.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.shipping.dto.AddressDTO;
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


    // Ajouter une adresse pour un utilisateur
    @POST
    @Path("/add")
    @Transactional
    public Response addAddress(AddressDTO addressDTO) {
        Address createdAddress = addressService.addAddress(
                addressDTO.getStreet(),
                addressDTO.getPostalCode(),
                addressDTO.getCity(),
                addressDTO.getCountry());
        return Response.status(Response.Status.CREATED).entity(createdAddress).build();
    }

    // Trouver les adresses de l'utilisateur courant
    @GET
    public List<Address> getUserAddresses() {
        return addressService.getAddressesByUserId();
    }

    // Récupérer une adresse par son ID (pas besoin de vérifier l'userId ici)
    @GET
    @Path("/{addressId}")
    public Response getAddressById(@PathParam("addressId") UUID addressId) {
        Address address = addressService.getAddressById(addressId);
        return Response.status(Response.Status.OK).entity(address).build();
    }

    // Mettre à jour une adresse
    @PUT
    @Path("/{addressId}/update")
    @Transactional
    public Response updateAddress(@PathParam("addressId") UUID addressId, AddressDTO addressDTO) {
        Address updatedAddress = addressService.updateAddress(
                addressId,
                addressDTO.getStreet(),
                addressDTO.getPostalCode(),
                addressDTO.getCity(),
                addressDTO.getCountry());
        return Response.status(Response.Status.OK).entity(updatedAddress).build();
    }

    // Supprimer une adresse
    @DELETE
    @Path("/{addressId}/delete")
    @Transactional
    public Response deleteAddress(@PathParam("addressId") UUID addressId) {
        addressService.deleteAddress(addressId);
        return Response.noContent().build();
    }
}
