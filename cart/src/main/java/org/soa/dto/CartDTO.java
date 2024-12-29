package org.soa.dto;

import java.util.List;
import java.util.UUID;

public class CartDTO {

    private UUID cartId; // Identifiant du panier
    private List<ItemDTO> items; // Liste des items du panier

    // Constructeur sans paramètres
    public CartDTO() {
    }

    // Constructeur avec paramètres
    public CartDTO(UUID cartId, List<ItemDTO> items) {
        this.cartId = cartId;
        this.items = items;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
