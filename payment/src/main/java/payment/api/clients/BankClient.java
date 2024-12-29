package payment.api.clients;

import java.util.UUID;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface BankClient {

    @POST
    @Retry(maxRetries = 3, delay = 500, jitter = 200)
    @ClientHeaderParam(name = "X-API-Key", value = "${bank.api.key}")
    Response makeNewPayment(BankPaymentRequest bankPaymentRequest);

    @GET
    @Path("/{id}")
    @ClientHeaderParam(name = "X-API-Key", value = "${bank.api.key}")
    Response paymentById(@PathParam("id") UUID paymentId);
}