package com.controllers;

import java.util.List;

import com.entities.ShippingOrder;
import com.services.ShippingOrderService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/shipping/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingOrderController {
    private final ShippingOrderService orderService = new ShippingOrderService();

    @POST
    public Response createOrder(ShippingOrder order) {
        ShippingOrder createdOrder = orderService.createOrder(order);
        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("orderId") Long orderId) {
        ShippingOrder order = orderService.getOrderById(orderId);
        if (order != null) {
            return Response.ok(order).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response getAllOrders() {
        List<ShippingOrder> orders = orderService.getAllOrders();
        return Response.ok(orders).build();
    }

    @PUT
    @Path("/{orderId}")
    public Response updateOrder(@PathParam("orderId") Long orderId, ShippingOrder order) {
        ShippingOrder updatedOrder = orderService.updateOrder(orderId, order);
        if (updatedOrder != null) {
            return Response.ok(updatedOrder).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{orderId}")
    public Response deleteOrder(@PathParam("orderId") Long orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
