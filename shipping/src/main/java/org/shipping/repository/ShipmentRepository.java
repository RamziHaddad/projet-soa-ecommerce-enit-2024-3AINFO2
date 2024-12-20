package org.shipping.repository;

import org.shipping.model.Shipment;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShipmentRepository implements PanacheRepository<Shipment> {

}
