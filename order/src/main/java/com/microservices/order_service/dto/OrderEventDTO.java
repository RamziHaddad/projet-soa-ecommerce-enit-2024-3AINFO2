package com.microservices.order_service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.microservices.order_service.model.Item;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEventDTO {
    private UUID orderId;
    private List<Item> items;
    private String orderStatus;

    public String convertToJson() {
        StringBuilder jsonString = new StringBuilder();

        jsonString.append("{");

        // Add orderId field (UUID to String)
        jsonString.append("\"orderId\": \"").append(orderId.toString()).append("\",");

        // Add items field (List of Item objects)
        jsonString.append("\"items\": [");
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                jsonString.append("{");
                jsonString.append("\"itemId\": \"").append(item.getItemId()).append("\",");
                jsonString.append("\"quantity\": ").append(item.getQuantity());
                jsonString.append("}");
                if (i < items.size() - 1) {
                    jsonString.append(","); // Add comma if it's not the last item
                }
            }
        }
        jsonString.append("],");

        // Add orderStatus field
        jsonString.append("\"orderStatus\": \"").append(orderStatus).append("\"");

        // Close the JSON object
        jsonString.append("}");

        return jsonString.toString();
    }



}
