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
@Table(name="Tiered_Discount")
public class TieredDiscount {
    
    @Id 
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="tieredDiscount_id", nullable=false)
	private UUID tieredDiscountId;
	
    @Column(name="start_date")
	private Date startDate; 

    @Column(name="end_date")
	private Date endDate; 

    @Column(name="threshold_amount")
	private Date thresholdAmount; 


    public UUID getTieredDiscountId() {
        return tieredDiscountId;
    }
    public void setTieredDiscountId(UUID tieredDiscountId) {
        this.tieredDiscountId = tieredDiscountId;
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


    public Date getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(Date thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

}
