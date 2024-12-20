package org.soa.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.soa.dto.CartDTO;

public class CartDTOSerializer extends ObjectMapperSerializer<CartDTO> {

    /**
     * Constructor for CartDTOSerializer.
     * This ensures the ObjectMapper provided by Quarkus is used to serialize CartDTO objects.
     */
    public CartDTOSerializer() {
        super();
    }
}
