package payment.api.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import payment.api.dto.OrderUpdateRequestDTO;

@RegisterRestClient(baseUri = "http://localhost:8084/")
@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderClient {
    @POST
    @Path("/update-status")
    Response updateOrderStatus(OrderUpdateRequestDTO orderUpdateRequestDTO);
}
