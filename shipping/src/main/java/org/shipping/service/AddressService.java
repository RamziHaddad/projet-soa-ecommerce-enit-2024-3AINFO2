package org.shipping.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.shipping.model.Address;
import org.shipping.repository.AddressRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AddressService {

    @Inject
    AddressRepository addressRepository;

    @Inject
    SecurityService securityService;

    // Récupérer une adresse par son ID
    public Address getAddressById(UUID addressId) {
        return addressRepository.find("addressId", addressId).firstResult();
    }

    // Ajouter une adresse pour un utilisateur
    @Transactional
    public Address addAddress(String street, String postalCode, String city, String country) {
        // UUID userId = securityService.getCurrentUserId(); // Extraire l'userId du JWT
        UUID userId = UUID.fromString("faa1b47d-27e3-4106-b42b-2d1e7d1f6e93");
        Address address = new Address(userId, street, postalCode, city, country);
        addressRepository.persist(address);
        return address;
    }

    // Trouver les adresses d'un utilisateur
    public List<Address> getAddressesByUserId() {
        UUID userId = securityService.getCurrentUserId(); // Extraire l'userId du JWT
        return addressRepository.find("userId", userId).list();
    }

    // Mettre à jour une adresse
    @Transactional
    public Address updateAddress(UUID addressId, String street, String postalCode, String city, String country) {
        Address address = getAddressById(addressId);
        if (addressRepository.isPersistent(address)) {
            address.setStreet(street);
            address.setPostalCode(postalCode);
            address.setCity(city);
            address.setCountry(country);
            addressRepository.persist(address);
        }
        return address;
    }

    // Supprimer une adresse
    @Transactional
    public boolean deleteAddress(UUID addressId) {
        Address address = getAddressById(addressId);
        if (addressRepository.isPersistent(address)) {
            addressRepository.delete(address);
            return true;
        }
        return false;
    }
}
