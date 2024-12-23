package org.shipping.messaging;

import java.util.UUID;

import org.shipping.model.Address;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class AddressEventPublisher {

    @Inject
    Event<String> addressEvent;

    public void publishAddressAddedEvent(Address address) {
        addressEvent.fire("Address added: " + address.getAddressId());
    }

    public void publishAddressUpdatedEvent(Address address) {
        addressEvent.fire("Address updated: " + address.getAddressId());
    }

    public void publishAddressDeletedEvent(UUID addressId) {
        addressEvent.fire("Address deleted: " + addressId);
    }
}
