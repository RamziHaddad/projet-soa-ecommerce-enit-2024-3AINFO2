package com.enit.pricing.domain;


import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="Category_Discount")
public class CategoryDiscount {

    @Id 
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="categoryDiscount_id", nullable=false)
	private UUID categoryDiscountId;
	
    @Column(name="category")
	private String category;


    public UUID getCategoryDiscountId() {
        return categoryDiscountId;
    }
    public void setCategoryDiscountId(UUID categoryDiscountId) {
        this.categoryDiscountId = categoryDiscountId;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    } 

   

}
