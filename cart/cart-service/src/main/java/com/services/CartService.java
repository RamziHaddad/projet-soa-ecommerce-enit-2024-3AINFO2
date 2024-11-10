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
        cart.getItems().put(item.getItemId(), item);
        return cart;
    }

    // Supprimer un article d'un panier
    public Cart removeItemFromCart(UUID cartId, UUID itemId) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }
        cart.getItems().remove(itemId);
        return cart;
    }

    // Mettre à jour un article dans le panier
    public Cart updateItemInCart(UUID cartId, UUID itemId, Item item) {
        Cart cart = carts.get(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Panier non trouvé");
        }
        cart.getItems().put(itemId, item);
        return cart;
    }
}
