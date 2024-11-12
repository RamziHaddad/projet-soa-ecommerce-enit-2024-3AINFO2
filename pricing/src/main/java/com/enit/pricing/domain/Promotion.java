package com.enit.pricing.domain;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name= "promotion")
public class Promotion {
	@Id 
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="promotion_id", nullable=false)
	private UUID promotionId;
	
	@Column(name="start_date")
	private Date startDate; 

	@Column(name="end_date")
	private Date endDate; 

	@Column(name="reduction_percentage") 
	private float reductionPercentage; 

	@Column(name="min_quantity") 
	private int minQty ; 
/*
 * The minimum quantity of items bought from the same product to receive an offer; 
 * for example, if you buy minQte = 3 of product A, you will get a 20% discount.
 */


	@Column(name="promotion_description", columnDefinition = "TEXT") 
	private String description;

	@ManyToMany(mappedBy = "promotions") 
    private Set<Product> products;
	
	public UUID getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(UUID promotionId) {
		this.promotionId = promotionId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public float getReductionPercentage() {
		return reductionPercentage;
	}
	public void getReductionPercentage(float reductionPercentage) {
		this.reductionPercentage = reductionPercentage;
	}
	public int getMinQty() {
		return minQty;
	}
	public void setMinQty(int minQty) {
		this.minQty = minQty;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String type) {
		this.description = type;
	}

	public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }



}
	
	