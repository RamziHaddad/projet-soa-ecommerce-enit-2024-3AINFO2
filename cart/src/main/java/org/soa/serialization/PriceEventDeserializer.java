package org.soa.serialization;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;
import org.soa.dto.PriceEvent;


public class PriceEventDeserializer implements Deserializer<PriceEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuration si nécessaire
    }

    @Override
    public PriceEvent deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, PriceEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la désérialisation de l'événement de prix", e);
        }
    }

    @Override
    public void close() {
        // Nettoyage si nécessaire
    }
}
