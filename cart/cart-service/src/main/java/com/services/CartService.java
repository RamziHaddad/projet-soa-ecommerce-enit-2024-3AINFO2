package com.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.entites.Cart;
import com.entites.Item;

public class CartService {

    private Map<UUID, Cart> carts = new HashMap<>();

    // Créer un panier
    public Cart createCart(UUID cartId) {
        if (carts.containsKey(cartId)) {
            throw new IllegalArgumentException("Le panier avec cet ID existe déjà");
        }
        Cart cart = new Cart(cartId);
        carts.put(cartId, cart);
        return cart;
    }

    // Récupérer un panier par son ID
    public Cart getCart(UUID cartId) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }
        return cart;
    }

    // Ajouter un article dans un panier
    public Cart addItemToCart(UUID cartId, Item item) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }

        Map<UUID, Item> items = cart.getItems();
        UUID itemId = item.getItemId();

        if (items.containsKey(itemId)) {
            // Si l'article existe déjà, on incrémente sa quantité
            Item existingItem = items.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            // Sinon, on ajoute l'article
            items.put(itemId, item);
        }

        return cart;
    }

    // Supprimer un article d'un panier
    public Cart removeItemFromCart(UUID cartId, UUID itemId) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }

        Map<UUID, Item> items = cart.getItems();
        if (items.containsKey(itemId)) {
            Item existingItem = items.get(itemId);
            if (existingItem.getQuantity() > 1) {
                // Si la quantité est supérieure à 1, on décrémente
                existingItem.setQuantity(existingItem.getQuantity() - 1);
            } else {
                // Sinon, on retire complètement l'article
                items.remove(itemId);
            }
        } else {
            throw new IllegalArgumentException("Article non trouvé dans le panier");
        }

        return cart;
    }

    // Mettre à jour un article dans le panier
    public Cart updateItemInCart(UUID cartId, UUID itemId, Item item) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }

        Map<UUID, Item> items = cart.getItems();
        if (!items.containsKey(itemId)) {
            throw new IllegalArgumentException("Article non trouvé dans le panier");
        }

        // Mise à jour de l'article
        items.put(itemId, item);
        return cart;
    }
}
