package org.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Category {
    @Id @GeneratedValue
    private UUID idCategory;
    private String name;

    public Category(){}

    public  Category(UUID id,String name){
        this.idCategory=id;
        this.name=name;
    }

    public UUID getId() {
        return idCategory;
    }

    public void setId(UUID id) {
        this.idCategory = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
