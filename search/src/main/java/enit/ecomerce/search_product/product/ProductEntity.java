package enit.ecomerce.search_product.product;
import jakarta.persistence.Id;


import enit.ecomerce.search_product.consumer.ProductListed;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

//the reason to have 2 producst is that this one is specialy for a postre data base 
//in order ti implmeent the inbox, class product is mapped to elastic saerch 
//we cant map one class to 2 data bases
 
 @Entity
 @Table(name = "inbox")
public class ProductEntity {

    @Id
    private String id;

   
    private String name;

    private String description;

    private Float price;

    private String category; 
 
    private boolean isIndex;
    public boolean isIndex() {
        return isIndex;
    }



    public void setisIndex(boolean isIndex){
        this.isIndex=isIndex;
    }
    public ProductEntity( ) { 
       
    }

        public ProductEntity(ProductListed productListed,boolean isIndexed) {
        this.id = productListed.getAggregateID();
        this.name = productListed.getProductName();
        this.description = productListed.getDescription();
        this.price = (float) productListed.getPrice();  
        this.category = productListed.getCategoryName();
        this.isIndex = isIndexed;  
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




    public void setIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }
}
