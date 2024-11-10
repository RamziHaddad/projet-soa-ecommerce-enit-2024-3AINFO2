package domain;
import java.util.Date;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "promotion")
public class promotion {
	@Id 
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="promotion_id", nullable=false)
	private UUID promotionId;	
	private int productId ; 
	private Date startDate; 
	private Date endDate;  
	private float redPourcentage; 
	private int minQty ; 
	private String type;
	
	
	public UUID getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(UUID promotionId) {
		this.promotionId = promotionId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
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
	public float getRedPourcentage() {
		return redPourcentage;
	}
	public void setRedPourcentage(float redPourcentage) {
		this.redPourcentage = redPourcentage;
	}
	public int getMinQty() {
		return minQty;
	}
	public void setMinQty(int minQty) {
		this.minQty = minQty;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
	
	