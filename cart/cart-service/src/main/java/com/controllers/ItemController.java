package com.controllers;


import java.util.UUID;

import com.entites.Item;
import com.services.ItemService;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.*;

@Path("/items")
public class ItemController {

    private ItemService itemService = new ItemService();

    // Ajouter un article
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(Item item) {
        try {
            Item addedItem = itemService.addItem(item);
            return Response.status(Response.Status.CREATED).entity(addedItem).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Récupérer un article
    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("itemId") UUID itemId) {
        Item item = itemService.getItem(itemId);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Article non trouvé").build();
        }
        return Response.ok(item).build();
    }

    // Mettre à jour un article
    @PUT
    @Path("/{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("itemId") UUID itemId, Item item) {
        try {
            Item updatedItem = itemService.updateItem(itemId, item);
            return Response.ok(updatedItem).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Supprimer un article
    @DELETE
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("itemId") UUID itemId) {
        try {
            itemService.deleteItem(itemId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
