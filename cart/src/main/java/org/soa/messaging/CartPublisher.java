package org.soa.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.soa.dto.CartDTO;
import org.soa.dto.ItemDTO;

@ApplicationScoped
public class CartPublisher {

    @Inject
    @Channel("cart-topic") // Le nom du topic Kafka configuré
    Emitter<CartDTO> cartEmitter;

    public void publishCart(UUID cartId, List<ItemDTO> items) {
        // Construire un objet CartDTO à partir des paramètres
        CartDTO cartDTO = new CartDTO(cartId, items);
        // Envoyer le message au topic Kafka
        cartEmitter.send(cartDTO);
    }
}
