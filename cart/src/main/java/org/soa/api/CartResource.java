package org.soa.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;
import org.soa.model.Cart;
import org.soa.model.Item;
import org.soa.service.CartService;

import java.util.UUID;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @Inject
    CartService cartService;

    private static final Logger logger = Logger.getLogger(CartResource.class);

    // Créer un panier pour un utilisateur donné
    @POST
    @Path("/create/{userId}")
    public Response createCart(@PathParam("userId") UUID userId) {
        try {
            logger.info("Creating cart for User ID: " + userId);

            Cart cart = cartService.createCart(userId);
            logger.info("Cart created successfully with ID: " + cart.getCartId());

            return Response.status(Status.CREATED).entity(cart).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating cart: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Récupérer le panier d'un utilisateur
    @GET
    @Path("/get/{userId}")
    public Response getCart(@PathParam("userId") UUID userId) {
        try {
            logger.info("Fetching cart for User ID: " + userId);

            Cart cart = cartService.getCart(userId);
            if (cart == null) {
                logger.warn("No cart found for User ID: " + userId);
                return Response.status(Status.NOT_FOUND).entity("Cart not found").build();
            }

            return Response.ok(cart).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching cart: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Récupérer les items du panier d'un utilisateur
    @GET
    @Path("/{userId}/items")
    public Response getItems(@PathParam("userId") UUID userId) {
        try {
            logger.info("Fetching items for cart of User ID: " + userId);

            Cart cart = cartService.getCart(userId);
            if (cart == null) {
                logger.warn("No cart found for User ID: " + userId);
                return Response.status(Status.NOT_FOUND).entity("Cart not found").build();
            }

            return Response.ok(cart.getItems()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching items: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Ajouter un item au panier d'un utilisateur
    @POST
    @Path("/{userId}/add-item")
    public Response addItem(@PathParam("userId") UUID userId, Item cartItem) {
        try {
            logger.info("Adding item to cart for User ID: " + userId);

            cartService.addItem(userId, cartItem);
            logger.info("Item added successfully to cart of User ID: " + userId);

            return Response.status(Status.CREATED).entity("Item added to cart").build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input: " + e.getMessage());
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while adding item: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Mettre à jour un item dans le panier d'un utilisateur
    @PUT
    @Path("/{userId}/update-item")
    public Response updateItem(@PathParam("userId") UUID userId, Item updatedItem) {
        try {
            logger.info("Updating item in cart for User ID: " + userId);

            cartService.updateItem(userId, updatedItem);
            logger.info("Item updated successfully in cart of User ID: " + userId);

            return Response.ok("Item updated in cart").build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating item: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // Supprimer un item du panier d'un utilisateur
    @DELETE
    @Path("/{userId}/remove-item/{productId}")
    public Response removeItem(@PathParam("userId") UUID userId, @PathParam("productId") UUID productId) {
        try {
            logger.info("Removing item with Product ID: " + productId + " from cart of User ID: " + userId);

            cartService.removeItem(userId, productId);
            logger.info("Item removed successfully from cart of User ID: " + userId);

            return Response.status(Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input: " + e.getMessage());
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Unexpected error occurred while removing item: " + e.getMessage(), e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/{userId}/validate")
    public Response validateCart(@PathParam("userId") UUID userId) {
        cartService.validateCart(userId);
        return Response.status(Status.OK).entity("Cart Validated Successfully").build();
    }

    // Vider le panier d'un utilisateur
    @DELETE
    @Path("/{userId}/clear")
    public Response clearCart(@PathParam("userId") UUID userId) {
       try {
           logger.info("Clearing cart for User ID: " + userId);

           cartService.clearCart(userId);
           logger.info("Cart cleared successfully for User ID: " + userId);

           return Response.status(Status.NO_CONTENT).build();
       } catch (IllegalArgumentException e) {
           logger.warn("Invalid input: " + e.getMessage());
           return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
       } catch (Exception e) {
           logger.error("Unexpected error occurred while clearing cart: " + e.getMessage(), e);
           return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
       }
    }
}
