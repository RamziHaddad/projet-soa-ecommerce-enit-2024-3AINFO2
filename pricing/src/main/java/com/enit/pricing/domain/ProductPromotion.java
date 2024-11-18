package com.enit.pricing.domain;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;




@Entity 
@DiscriminatorValue("product_promotion")
public class ProductPromotion extends Promotion{
   
	@ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
	
    
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}