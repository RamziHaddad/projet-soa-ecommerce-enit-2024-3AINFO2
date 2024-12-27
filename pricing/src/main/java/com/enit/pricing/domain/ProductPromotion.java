package com.enit.pricing.domain;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;




@Entity 
@DiscriminatorValue("product_promotions")
public class ProductPromotion extends Promotion{
	
    @JsonIgnore
	@OneToMany(mappedBy = "promotion", cascade = CascadeType.PERSIST)
	private List<Product> products;
	
    
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}