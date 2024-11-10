package domain;

import java.util.Date;
import java.util.UUID;

public class promotionDto {
	
    private UUID promotionId;
	private int productId ; 
	private Date startDate; 
	private Date endDate;  
	private float redPourcentage; 
	private int minQty ; 
	private String type;
}
