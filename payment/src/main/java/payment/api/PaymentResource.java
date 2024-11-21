package payment.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import payment.api.dto.PaymentRequestDTO;
import payment.api.dto.PaymentResponseDTO;

import payment.services.PaymentService;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    @Inject
    private PaymentService paymentService;
     @POST
    public Response processPayment(PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
     @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") UUID paymentId) {
        Optional<PaymentResponseDTO> response = paymentService.getPaymentById(paymentId);
        if (response.isPresent()) {
            return Response.ok(response.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
     @DELETE
    @Path("/{id}")
    public Response cancelPayment(@PathParam("id") UUID paymentId) {
        boolean isCancelled = paymentService.cancelPayment(paymentId);
        if (isCancelled) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Payment cannot be cancelled. Either it does not exist or is not in PENDING status.")
                    .build();
        }
    }
    @GET
    public Response getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return Response.ok(payments).build();
    }
     @GET
    @Path("/by-date")
    public List<PaymentResponseDTO> getPaymentsByDate(@QueryParam("date") String date, @QueryParam("customerId") UUID customerId) {
        
            LocalDateTime parsedDate = LocalDateTime.parse(date);
            List<PaymentResponseDTO> payments = paymentService.getPaymentsByDate(parsedDate, customerId);
            return payments ; 
        
    }
}
