package org.shipping.model;

import java.util.HashMap;
import java.util.Map;

public class EventTypeMapper {
    // Dictionnaire pour mappage d'événements
    private static final Map<String, String> eventTypes = new HashMap<>();

    static {
        // Vous pouvez ajouter autant d'événements que vous le souhaitez
        eventTypes.put("SHIPPING_CREATED", "shipping.created");
        eventTypes.put("SHIPPING_UPDATED", "shipping.updated");
        eventTypes.put("SHIPPING_DELETED", "shipping.deleted");
        // Vous pouvez ajouter d'autres événements ici
    }

    // Récupère le type d'événement en fonction du nom
    public static String getEventType(String eventType) {
        return eventTypes.getOrDefault(eventType, "unknown.event");
    }
}
