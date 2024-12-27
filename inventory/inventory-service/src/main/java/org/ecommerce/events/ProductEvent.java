package org.ecommerce.events;
import org.ecommerce.model.Category;
import java.util.UUID;


//The ProductEvent will be sent for the events that require knowing the hole product
public class ProductEvent extends MinimalEvent {

    private int totalQuantity;
    private int reservedQuantity;
    private String name;
    private Category category;

    public ProductEvent(UUID productId, int totalQuantity, int reservedQuantity,String name, Category category, String eventType) {
        super(productId,eventType);

        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
        this.name=name;
        this.category=category;

    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
