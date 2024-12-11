package org.soa.api;

import java.util.UUID;

import org.soa.Kafka.dto.CartDTO;
import org.soa.Kafka.messaging.CartProducer;
import org.soa.model.Cart;
import org.soa.model.Item;
import org.soa.service.CartService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @Inject
    CartService cartService;

    // Créer un panier pour un utilisateur donné
    @POST
    @Path("/create/{userId}")
    public Response createCart(@PathParam("userId") UUID userId) {
        Cart cart = cartService.createCart(userId);
        CartMessageDTO kafkaMessage = new CartMessageDTO(userId, "Cart created", cart.toDTO());
        return Response.status(Response.Status.CREATED)
                .entity(kafkaMessage)
                .build();
    }

    // Récupérer un panier pour un utilisateur donné
    @GET
    @Path("/get/{userId}")
    public Response getCart(@PathParam("userId") UUID userId) {
        Cart cart = cartService.getCart(userId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
        }
        return Response.ok(cart.toDTO()).build();
    }

    // Récupérer tous les items d'un panier
    @GET
    @Path("/{userId}/items")
    public Response getItems(@PathParam("userId") UUID userId) {
        Cart cart = cartService.getCart(userId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cart not found").build();
        }
        return Response.ok(cart.getItems()).build();
    }

    // Ajouter un item à un panier
    @POST
    @Path("/{userId}/add-item")
    public Response addItem(@PathParam("userId") UUID userId, Item cartItem) {
        cartService.addItem(userId, cartItem);
        return Response.status(Response.Status.CREATED).entity("Item added to cart").build();
    }

    // Mettre à jour un item dans un panier
    @PUT
    @Path("/{userId}/update-item")
    public Response updateItem(@PathParam("userId") UUID userId, Item updatedItem) {
        cartService.updateItem(userId, updatedItem);
        return Response.ok("Item updated in cart").build();
    }

    // Supprimer un item d'un panier
    @DELETE
    @Path("/{userId}/remove-item/{productId}")
    public Response removeItem(@PathParam("userId") UUID userId, @PathParam("productId") UUID productId) {
        try {
            cartService.removeItem(userId, productId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Vider un panier
    @DELETE
    @Path("/{userId}/clear")
    public Response clearCart(@PathParam("userId") UUID userId) {
        try {
            cartService.clearCart(userId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    @Inject
    CartProducer cartProducer;

    @POST
    @Path("/publish")
    public Response publishCart(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setItems(cart.getItems());
        cartProducer.sendCartMessage(cartDTO);
        return Response.ok("Cart published!").build();
    }
}
