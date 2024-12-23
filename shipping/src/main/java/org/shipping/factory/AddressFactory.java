package org.shipping.factory;

import java.util.UUID;

import org.shipping.model.Address;

public class AddressFactory {

    public static Address createAddress(UUID userId, String street, String postalCode, String city, String country) {
        return new Address(userId, street, postalCode, city, country);
    }
}










