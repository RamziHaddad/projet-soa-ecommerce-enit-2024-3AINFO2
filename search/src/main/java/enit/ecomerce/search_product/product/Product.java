package enit.ecomerce.search_product.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import enit.ecomerce.search_product.consumer.ProductReceived;

@Document(indexName = "product")
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Text)   
    private String name;

    @Field(type = FieldType.Text)  
    private String description;

    @Field(type = FieldType.Float)   
    private Float price;

    @Field(type = FieldType.Text)   
    private String category; 

    public Product( ) { 
       
    }

    // Constructor for transforming ProductReceived data into a Product entity.
    // This constructor is not part of the Elasticsearch mapping.
    public Product(ProductReceived productReceived) { 
        this.id = productReceived.id();
        this.name = productReceived.name();
        this.description = productReceived.description();
        this.price = productReceived.price();
        this.category = productReceived.category();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
