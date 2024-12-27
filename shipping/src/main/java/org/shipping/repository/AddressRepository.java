package org.shipping.repository;

import org.shipping.model.Address;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheRepository<Address> {
    
}
