package org.shipping.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;
import org.shipping.factory.AddressFactory;
import org.shipping.messaging.AddressEventPublisher;
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

    private static final Logger logger = Logger.getLogger(AddressService.class);

    // Récupérer une adresse par son ID
    public Address getAddressById(UUID addressId) {
        try {
            if (addressId == null) {
                throw new IllegalArgumentException("Address ID cannot be null.");
            }

            // Recherche de l'adresse
            Address address = addressRepository.find("addressId", addressId).firstResult();
            if (address == null) {
                throw new NoResultException("No address found for ID: " + addressId);
            }

            return address;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: " + e.getMessage(), e);
            throw new IllegalArgumentException("Invalid input: " + e.getMessage());
        } catch (NoResultException e) {
            logger.warn("Address not found: " + e.getMessage(), e);
            throw new NoResultException("Address not found: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while retrieving the address.", e);
            throw new RuntimeException("An unexpected error occurred while retrieving the address.");
        }
    }

    

    // Trouver les adresses d'un utilisateur
    public List<Address> getAddressesByUserId() {
        try {
            // UUID userId = securityService.getCurrentUserId();
            UUID userId = UUID.fromString("faa1b47d-27e3-4106-b42b-2d1e7d1f6e93"); // Simulé pour le moment
            if (userId == null) {
                throw new IllegalArgumentException("User ID could not be extracted from the token.");
            }

            List<Address> addresses = addressRepository.find("userId", userId).list();
            if (addresses.isEmpty()) {
                throw new NoResultException("No addresses found for the current user.");
            }

            return addresses;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid user context: " + e.getMessage(), e);
            throw new IllegalArgumentException("Invalid user context: " + e.getMessage());
        } catch (NoResultException e) {
            logger.warn("No addresses found: " + e.getMessage(), e);
            throw new NoResultException("No addresses found: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching user addresses.", e);
            throw new RuntimeException("An unexpected error occurred while fetching user addresses.");
        }
    }
   


    // Mettre à jour une adresse
    @Transactional
    public Address updateAddress(UUID addressId, String street, String postalCode, String city, String country) {
        try {
            if (addressId == null) {
                throw new IllegalArgumentException("Address ID cannot be null.");
            }

            Address address = getAddressById(addressId);
            if (address == null) {
                throw new NoResultException("Address not found for ID: " + addressId);
            }

            logger.info("Updating address with ID: " + addressId);

            if (street != null)
                address.setStreet(street);
            if (postalCode != null)
                address.setPostalCode(postalCode);
            if (city != null)
                address.setCity(city);
            if (country != null)
                address.setCountry(country);

            addressRepository.persist(address);
            return address;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input while updating address: " + e.getMessage(), e);
            throw new IllegalArgumentException("Invalid input while updating address: " + e.getMessage());
        } catch (NoResultException e) {
            logger.warn("Address not found during update: " + e.getMessage(), e);
            throw new NoResultException("Address not found during update: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating the address.", e);
            throw new RuntimeException("An unexpected error occurred while updating the address.");
        }
    }

   
    
    @Inject
AddressEventPublisher eventPublisher;

@Transactional
public Address addAddress(String street, String postalCode, String city, String country) {
    UUID userId = UUID.fromString("faa1b47d-27e3-4106-b42b-2d1e7d1f6e93");
    Address address = AddressFactory.createAddress(userId, street, postalCode, city, country);
    addressRepository.persist(address);
    eventPublisher.publishAddressAddedEvent(address); // Publier l'événement
    return address;
}

@Transactional
public boolean deleteAddress(UUID addressId) {
    Address address = getAddressById(addressId);
    if (addressRepository.isPersistent(address)) {
        addressRepository.delete(address);
        eventPublisher.publishAddressDeletedEvent(addressId); // Publier l'événement
        return true;
    }
    return false;
}

}
