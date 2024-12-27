package com.enit.pricing.domain;

import java.math.BigDecimal;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;



@Entity
@DiscriminatorValue("tiered_discount")
public class TieredPromotion extends Promotion{
    
	@Column(name="threshold_amount")
    private BigDecimal thresholdAmount;

   public BigDecimal getThresholdAmount() {
       return thresholdAmount;
   }

   public void setThresholdAmount(BigDecimal thresholdAmount) {
       this.thresholdAmount = thresholdAmount;
   } 
}
