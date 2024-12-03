package enit.ecomerce.search_product.consumer;
import java.util.UUID;

import enit.ecomerce.search_product.product.Product;

public class ProductListed extends Event { 
    private   String productName;
    private   String categoryName;
    private   String description;
    private   double price;

    public ProductListed(String eventType, String aggregateType, String aggregateId) {
        super(eventType,aggregateType,aggregateId);
    }
    public ProductListed( ) {
        super( );
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

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    } 
    @Override
    public String toString() {
        return "ProductListed{" +
                "productId='" + super.aggregateType + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}