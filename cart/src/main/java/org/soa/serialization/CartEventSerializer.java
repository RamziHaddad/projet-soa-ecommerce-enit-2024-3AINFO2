package org.soa.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

import org.soa.dto.CartMessage;

public class CartEventSerializer extends ObjectMapperSerializer<CartMessage> {

}
