package com.controllers;

import java.util.Map;

import com.entities.ShippingAddress;
import com.services.ShippingAddressService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/shipping/addresses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingAddressController {
    private final ShippingAddressService addressService = new ShippingAddressService();

    @POST
    public Response createAddress(ShippingAddress address) {
        ShippingAddress createdAddress = addressService.createAddress(address);
        return Response.status(Response.Status.CREATED).entity(createdAddress).build();
    }

    @GET
    @Path("/{addressId}")
    public Response getAddress(@PathParam("addressId") Long addressId) {
        ShippingAddress address = addressService.getAddressById(addressId);
        if (address != null) {
            return Response.ok(address).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response getAllAddresses() {
        Map<Long, ShippingAddress> addresses = addressService.getAllAddresses();
        return Response.ok(addresses.values()).build();
    }

    @DELETE
    @Path("/{addressId}")
    public Response deleteAddress(@PathParam("addressId") Long addressId) {
        boolean deleted = addressService.deleteAddress(addressId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
