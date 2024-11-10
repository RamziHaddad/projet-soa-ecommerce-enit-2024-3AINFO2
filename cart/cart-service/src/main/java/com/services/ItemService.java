package com.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.entites.Item;

public class ItemService {

    private Map<UUID, Item> items = new HashMap<>();

    // Ajouter un article
    public Item addItem(Item item) {
        if (items.containsKey(item.getItemId())) {
            throw new IllegalArgumentException("Cet article existe déjà");
        }
        items.put(item.getItemId(), item);
        return item;
    }

    // Récupérer un article par son ID
    public Item getItem(UUID itemId) {
        return items.get(itemId);
    }

    // Mettre à jour un article
    public Item updateItem(UUID itemId, Item item) {
        if (!items.containsKey(itemId)) {
            throw new IllegalArgumentException("Article non trouvé");
        }
        items.put(itemId, item);
        return item;
    }

    // Supprimer un article
    public void deleteItem(UUID itemId) {
        if (!items.containsKey(itemId)) {
            throw new IllegalArgumentException("Article non trouvé");
        }
        items.remove(itemId);
    }
}

