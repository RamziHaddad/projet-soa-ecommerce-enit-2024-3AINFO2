package org.soa.dto;

public class PriceEvent {
    private String productId;
    private double price;

    // Getters et setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PriceEvent{" +
                "productId='" + productId + '\'' +
                ", price=" + price +
                '}';
    }
}

