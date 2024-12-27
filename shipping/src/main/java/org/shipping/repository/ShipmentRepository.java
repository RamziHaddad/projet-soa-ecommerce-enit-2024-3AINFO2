package org.shipping.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import org.shipping.model.Shipment;

@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {
    // Now this repository has the persist method
}
