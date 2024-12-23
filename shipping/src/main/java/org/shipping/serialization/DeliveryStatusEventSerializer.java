package org.shipping.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;

import org.shipping.dto.DeliveryStatusMessage;

public class DeliveryStatusEventSerializer extends ObjectMapperSerializer<DeliveryStatusMessage> {

}
