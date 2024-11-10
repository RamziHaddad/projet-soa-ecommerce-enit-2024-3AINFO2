package com.entities;

import java.util.List;

public class ShippingAddress {
    private Long addressId;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private List<Long> orderIds; // Store only IDs of associated orders
    public Long getAddressId() {
        return addressId;
    }
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public List<Long> getOrderIds() {
        return orderIds;
    }
    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    
}
