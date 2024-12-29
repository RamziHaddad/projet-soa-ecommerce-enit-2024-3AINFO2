package com.microservices.order_service.kafka;

import com.microservices.order_service.dto.DeliveryStatusMessage;
import com.microservices.order_service.events.OrderPaidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class DeliveryEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryEventConsumer.class);

    private final RestTemplate restTemplate;

    @Value("${delivery.service.url}")
    private String deliveryServiceUrl;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;



    public DeliveryEventConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(
            topics = "delivery-status",
            groupId = "deliveryReceiver",
            containerFactory = "DeliveryListenerContainerFactory"
    )
    @Transactional
    public void consume(@Payload DeliveryStatusMessage deliveryStatusMessage,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
        logger.info("Received deliveryStatusMessage: {}", deliveryStatusMessage.getOrderId());
        logger.info("Received deliveryStatusMessage: {}", deliveryStatusMessage.getStatus());


    }
        /*try {
            logger.info("Received OrderPaidEvent: partition={}, offset={}, event={}",
                    partition, offset, event);

            // Validate the event
            validateEvent(event);

            // Verify address exists using the Delivery Service API
            String addressApiUrl = deliveryServiceUrl + "/api/address/" + event.getAddressId();
            ResponseEntity<String> addressResponse = restTemplate.getForEntity(addressApiUrl, String.class);
            if (!addressResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Address not found for ID: " + event.getAddressId());
            }
            logger.info("Address verified for ID: {}", event.getAddressId());

            // Create shipment using the Shipping Service API
            String shippingApiUrl = shippingServiceUrl + "/create";
            ResponseEntity<String> shipmentResponse = restTemplate.postForEntity(
                    shippingApiUrl,
                    event, // Send the event as the body (or create a DTO if required)
                    String.class
            );
            if (!shipmentResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to create shipment for Order ID: " + event.getOrderId());
            }
            logger.info("Successfully created shipment for order: {} and cart: {}",
                    event.getOrderId(), event.getCartId());

        } catch (IllegalArgumentException e) {
            logger.error("Invalid event data: {}", e.getMessage());
            handleInvalidEvent(event, e);
        } catch (Exception e) {
            logger.error("Error processing OrderPaidEvent", e);
            handleProcessingError(event, e);
        }
    }

    private void validateEvent(OrderPaidEvent event) {
        if (event.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (event.getCartId() == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }
        if (event.getAddressId() == null) {
            throw new IllegalArgumentException("Address ID cannot be null");
        }
    }

    private void handleInvalidEvent(OrderPaidEvent event, Exception e) {
        logger.error("Invalid event received: {}. Error: {}", event, e.getMessage());
        // Additional logic can be implemented here, such as publishing to a dead letter queue
    }

    private void handleProcessingError(OrderPaidEvent event, Exception e) {
        logger.error("Error processing event: {}. Error: {}", event, e.getMessage());
        // Implement retry logic or publish to a retry topic if needed
    } */
}
