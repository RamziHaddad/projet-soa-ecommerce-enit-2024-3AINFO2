package org.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ecommerce.model.Item;
import org.ecommerce.model.OrderEventDTO;
import org.ecommerce.model.OrderStatusUpdateDTO;
import org.ecommerce.repository.OrderRepository;

import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class EventConsumerService {
    @Inject
    ProductService productService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    OrderRepository orderRepository;

    @Incoming("order-creation")//waiting for the true order topic
    @Transactional
    public void consumeOrderCreationEvent(String orderEventJSON)throws JsonProcessingException {
        try{
            OrderEventDTO order= objectMapper.readValue(orderEventJSON, OrderEventDTO.class);
            System.out.println("orderEventJSON:\t"+orderEventJSON);
            System.out.println("order object: "+order.toString());
            if(Objects.equals(order.getOrderStatus(), "CREATED")){

                orderRepository.addOrder(order);
                for(Item item :order.getItems()){
                    productService.reserveProduct(item.getItemId(), item.getQuantity());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Incoming("order-status-update")//waiting for the true order topic
    @Transactional
    public void consumeOrderStatusEvent(String orderEventJSON)throws JsonProcessingException {
        try{
            OrderStatusUpdateDTO orderStatusUpdateDTO = objectMapper.readValue(orderEventJSON, OrderStatusUpdateDTO.class);
            System.out.println("orderEventUpdateJSON:\t"+orderEventJSON);
            System.out.println("orderEventUpdateObject:\t"+orderStatusUpdateDTO);
            OrderEventDTO order=orderRepository.getOrderByID(orderStatusUpdateDTO.getOrderId())
                    .orElseThrow(() -> new WebApplicationException("Product not found", 404));
            if(Objects.equals(orderStatusUpdateDTO.getStatus(), "CANCELED")){
                order.setOrderStatus("CANCELED");
                orderRepository.updateOrder(order);
                for(Item item :order.getItems()){
                    productService.releaseReservation(item.getItemId(), item.getQuantity());
                }

            }
            else if(Objects.equals(orderStatusUpdateDTO.getStatus(), "PAID")){
                order.setOrderStatus("PAID");

                orderRepository.updateOrder(order);
                for(Item item :order.getItems()){
                    productService.recordOrderShipment(item.getItemId(), item.getQuantity());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
