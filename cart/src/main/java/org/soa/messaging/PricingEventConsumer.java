package org.soa.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.soa.dto.PriceEvent;

@ApplicationScoped
public class PricingEventConsumer {

    private final Map<UUID, Double> productPrices = new HashMap<>(); // Utilisation de UUID comme clé

    @Incoming("pricing-price-channel") // Le canal Kafka configuré dans application.properties
    public void consumePriceEvent(PriceEvent priceEvent) {
        // Enregistrer le prix de l'article avec l'UUID comme clé
        UUID productId = UUID.fromString(priceEvent.getProductId()); // Conversion de String en UUID
        productPrices.put(productId, priceEvent.getPrice());
        System.out.println("Prix reçu pour l'article : " + priceEvent);
    }

    public double getPrice(UUID productId) {
        return productPrices.getOrDefault(productId, -1.0); // Retourne -1.0 si le produit n'est pas trouvé
    }

    public Map<UUID, Double> getAllPrices() {
        return productPrices;
    }
}
