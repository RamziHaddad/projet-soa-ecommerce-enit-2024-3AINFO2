package com.controllers;

import java.util.UUID;

import com.entites.Cart;
import com.entites.Item;
import com.services.CartService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/carts")
public class CartController {

    private CartService cartService = new CartService();

    // Créer un panier
    @POST
    @Path("/create/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCart(@PathParam("cartId") UUID cartId) {
        try {
            Cart cart = cartService.createCart(cartId);
            return Response.status(Response.Status.CREATED).entity(cart).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Récupérer un panier par son ID
    @GET
    @Path("/{cartId}")
    @Produces(MediaType.APPLICATION_JSON) // Correction ici
    public Response getCart(@PathParam("cartId") UUID cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return Response.ok(cart).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Ajouter un article dans le panier
    @POST
    @Path("/{cartId}/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToCart(@PathParam("cartId") UUID cartId, Item item) {
        try {
            Cart cart = cartService.addItemToCart(cartId, item);
            return Response.ok(cart).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Supprimer un article du panier
    @DELETE
    @Path("/{cartId}/remove/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeItemFromCart(@PathParam("cartId") UUID cartId, @PathParam("itemId") UUID itemId) {
        try {
            Cart cart = cartService.removeItemFromCart(cartId, itemId);
            return Response.ok(cart).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Mettre à jour un article dans le panier
    @PUT
    @Path("/{cartId}/update/{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItemInCart(@PathParam("cartId") UUID cartId, @PathParam("itemId") UUID itemId, Item item) {
        try {
            Cart cart = cartService.updateItemInCart(cartId, itemId, item);
            return Response.ok(cart).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
