package org.shipping.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class EventMappingConfig {

    private final Map<UUID, String> eventMappings = new HashMap<>();

    public EventMappingConfig() {
        Config config = ConfigProvider.getConfig();
        config.getPropertyNames()
              .forEach(property -> {
                  if (property.startsWith("event.mapping.")) {
                      String uuidString = property.replace("event.mapping.", "");
                      String status = config.getValue(property, String.class);
                      eventMappings.put(UUID.fromString(uuidString), status);
                  }
              });
    }

    public String getDeliveryStatus(String eventType) {
        try {
            // Convert the eventType string to UUID
            UUID eventTypeUUID = UUID.fromString(eventType);
            return eventMappings.get(eventTypeUUID); // Return status if found
        } catch (IllegalArgumentException e) {
            // Handle invalid UUID format, log or return a default value
            return null;
        }
    }
}
