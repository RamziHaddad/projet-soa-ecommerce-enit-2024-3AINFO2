package org.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ecommerce.model.Item;
import org.ecommerce.model.Orders;
import org.ecommerce.model.OrderDTO;
import org.ecommerce.repository.OrderRepository;

import java.util.Objects;
import java.util.Optional;

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
            Orders order= objectMapper.readValue(orderEventJSON,Orders.class);
            System.out.println("orderEventJSON:\t"+orderEventJSON);
            orderRepository.addOrder(order);
            for(Item item :order.getItems()){
                productService.reserveProduct(item.getId(), item.getQuantity());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Incoming("order-status")//waiting for the true order topic
    @Transactional
    public void consumeOrderStatusEvent(String orderEventJSON)throws JsonProcessingException {
        try{
            OrderDTO orderDTO= objectMapper.readValue(orderEventJSON,OrderDTO.class);
            System.out.println("orderEventJSON:\t"+orderEventJSON);
            Orders order=orderRepository.getOrderByID(orderDTO.getOrderId())
                    .orElseThrow(() -> new WebApplicationException("Product not found", 404));
            if(Objects.equals(orderDTO.getStatus(), "canceled")){
                order.setStatus("canceled");
                orderRepository.updateOrder(order);
                for(Item item :order.getItems()){
                    productService.releaseReservation(item.getId(), item.getQuantity());
                }

            }
            else if(Objects.equals(orderDTO.getStatus(), "paid")){
                order.setStatus("paid");
                orderRepository.updateOrder(order);
                for(Item item :order.getItems()){
                    productService.recordOrderShipment(item.getId(), item.getQuantity());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
