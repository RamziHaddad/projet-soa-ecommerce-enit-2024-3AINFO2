package org.shipping.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class AddressUpdateDTO {

    private UUID addressId; // Optionnel si nécessaire
    @Size(max = 100, message = "Street cannot exceed 100 characters")
    private String street; // Rue de l'adresse

    @Pattern(regexp = "^[0-9]{4}$", message = "Postal code must be a 4-digit number.")
    private String postalCode; // Code postal

    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city; // Ville

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    private String country; // Pays

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
