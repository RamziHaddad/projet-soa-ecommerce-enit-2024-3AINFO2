package enit.ecomerce.search_product.consumer;

import enit.ecomerce.search_product.product.Product;

public class ProductListed extends Event { 
    private   String productName;
    private   String categoryName;
    private   String description;
    private   double price;

    public ProductListed() {
    }
    public ProductListed(Product product) {
        super("productListed", "Product", product.getId().toString());
        this.productName = product.getName();
        this.categoryName = product.getCategory();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    } 
    @Override
    public String toString() {
        return "ProductListed{" +
                "productId='" + this.eventId + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}

