package payment.api.clients;



import java.util.UUID;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
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
@RegisterRestClient(baseUri = "http://localhost:8099/")
public interface BankClient {

    @POST
    @Transactional
     @Retry(maxRetries = 3, delay = 500, jitter = 200)
     
    Response makeNewPayment(BankPaymentRequest bankPaymentRequest);
    @GET
    @Path("/{id}")
    Response paymentById(@PathParam("id") UUID paymentId);
}
