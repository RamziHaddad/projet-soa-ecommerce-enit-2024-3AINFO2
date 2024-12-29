package payment;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class PaymentApiTest {

    @Test
    public void testBulkPayments() {
        int numberOfRequests = 10; // Number of requests
        Random random = new Random();

        for (int i = 0; i < numberOfRequests; i++) {
            int cardNumber = 10000000 + random.nextInt(90000000); // Generate random 8-digit number
            int cardCode;

            // For half of the requests, cardCode is equal to cardNumber
            if (i % 2 == 0) {
                cardCode = cardNumber;
            } else {
                cardCode = 1000 + random.nextInt(9000); // Generate a 4-digit number different from cardNumber
            }

            String paymentJson = String.format(
                "{ \"amount\": 100, \"currency\": \"USD\", \"customerId\": \"%s\", \"cardNumber\": %d, \"cardCode\": %d }",
                UUID.randomUUID().toString(), cardNumber, cardCode
            );

            Response response = given()
                .contentType("application/json")
                .body(paymentJson)
                .when()
                .post("http://localhost:8085/payments");

            // Log request and response details for debugging
            System.out.println("Request payload: " + paymentJson);
            System.out.println("Response body: " + response.getBody().asString());

            // Assert the response status code is 201
            response.then().statusCode(201);
        }
    }
}
