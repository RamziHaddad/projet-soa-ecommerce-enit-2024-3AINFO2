package enit.ecomerce.search_product.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import enit.ecomerce.search_product.consumer.ProductListed;
//the reason to have 2 producst is that this one is specialy for a postre data base 
//in order ti implmeent the inbox, class product is mapped to elastic saerch 
//we cant map one class to 2 data bases
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

    // Constructor for transforming ProductListeddata into a Product entity.
    // This constructor is not part of the Elasticsearch mapping.
    public Product(ProductListed productListed) {
        this.id = productListed.getAggregateID();
        this.name = productListed.getProductName();
        this.description = productListed.getDescription();
        this.price = (float) productListed.getPrice();  
        this.category = productListed.getCategoryName();
    }
   
    public Product(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.name = productEntity.getName();
        this.description = productEntity.getDescription();
        this.price = (float) productEntity.getPrice();  
        this.category = productEntity.getCategory();
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
    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + ", category=" + category + ", getId()=" + getId() + ", getName()=" + getName() + ", getClass()="
                + getClass() + ", getDescription()=" + getDescription() + ", getPrice()=" + getPrice()
                + ", getCategory()=" + getCategory() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }
}
