package org.shipping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Address {

    private UUID addressId; // Utilisé pour l'identifiant de l'adresse (lecture seule)

    @NotBlank(message = "Street is mandatory")
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street; // Rue de l'adresse

    @NotBlank(message = "Postal Code is mandatory")
    @Pattern(regexp = "^[0-9]{4}$", message = "Postal code must be a 4-digit number.")
    private String postalCode; // Code postal

    @NotBlank(message = "City is mandatory")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city; // Ville

    @NotBlank(message = "Country is mandatory")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country; // Pays

    // Constructeur sans paramètres
    public Address() {
    }

    // Constructeur avec paramètres
    public Address(UUID addressId, String street, String postalCode, String city, String country) {
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
