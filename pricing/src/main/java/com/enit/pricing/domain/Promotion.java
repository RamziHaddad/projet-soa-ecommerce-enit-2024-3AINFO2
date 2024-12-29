package com.enit.pricing.domain;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;



@Entity
@Table(name= "promotions")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="discount_type") 

public abstract class Promotion {
	
    @Id
    @Column(updatable = false, nullable = false)
    private UUID promotionId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reduction_percentage")
    private BigDecimal reductionPercentage;

    @Column(name = "promotion_description", columnDefinition = "TEXT")
    private String description;

    
	public UUID getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(UUID promotionId) {
		this.promotionId = promotionId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getReductionPercentage() {
		return reductionPercentage;
	}
	public void setReductionPercentage(BigDecimal reductionPercentage) {
		this.reductionPercentage = reductionPercentage;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String type) {
		this.description = type;
	}

}
	
	