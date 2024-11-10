package com.services;

import java.util.HashMap;
import java.util.Map;

import com.entities.ShippingAddress;

public class ShippingAddressService {
    private Map<Long, ShippingAddress> addresses = new HashMap<>();
    private long currentId = 1;

    public ShippingAddress createAddress(ShippingAddress address) {
        address.setAddressId(currentId++);
        addresses.put(address.getAddressId(), address);
        return address;
    }

    public ShippingAddress getAddressById(Long addressId) {
        return addresses.get(addressId);
    }

    public Map<Long, ShippingAddress> getAllAddresses() {
        return new HashMap<>(addresses);
    }

    public boolean deleteAddress(Long addressId) {
        return addresses.remove(addressId) != null;
    }
}
