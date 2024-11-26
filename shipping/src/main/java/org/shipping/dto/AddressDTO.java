package org.shipping.dto;

import java.util.UUID;

public class AddressDTO {

    private UUID addressId; // Utilisé pour l'identifiant de l'adresse (lecture seule)
    private String street; // Rue de l'adresse
    private String postalCode; // Code postal
    private String city; // Ville
    private String country; // Pays

    // Constructeur sans paramètres
    public AddressDTO() {
    }

    // Constructeur avec paramètres
    public AddressDTO(UUID addressId, String street, String postalCode, String city, String country) {
        this.addressId = addressId;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    // Getters et setters
    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
