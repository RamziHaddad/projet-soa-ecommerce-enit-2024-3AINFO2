package com.enit.pricing.repositories;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.enit.pricing.domain.TieredPromotion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;



@Repository
public class TieredDiscountRepository {

     @Autowired
     private EntityManager entityManager;   


       @Transactional
       public void addPromotion(TieredPromotion promotion){
              entityManager.persist(promotion);
       }
           @Transactional

    public TieredPromotion findById(UUID promoId) {
        try {
            TieredPromotion promotion = entityManager.find(TieredPromotion.class, promoId);
            if (promotion == null) {
                throw new EntityNotFoundException("Promotion not found with ID: " + promoId);
            }
            return promotion;
        } catch (PersistenceException e) {
            throw new RuntimeException("Error fetching promotion: " + e.getMessage(), e);
        }
    }

       @Transactional
       public void deletePromotionById(UUID promoId) {
              TieredPromotion promotion = findById(promoId);
              if (promotion == null) {
              throw new EntityNotFoundException("Promotion not found with ID: " + promoId);
              }
              try {
              entityManager.remove(promotion);
              } catch (PersistenceException e) {
              throw new RuntimeException("Failed to delete promotion: " + e.getMessage(), e);
              }
       }

       @Transactional
       public void updateThreshold(UUID promotionId, BigDecimal threshold) {
              String query= "UPDATE TieredPromotion t SET t.thresholdAmount = :threshold WHERE t.promotionId = :promotionId";
              System.out.println("Im just right after the query");
              int updatedCount =entityManager.createQuery(query)
                                   .setParameter("threshold",threshold)
                                   .setParameter("promotionId", promotionId)
                                   .executeUpdate();

              System.out.println("after execution");
              if (updatedCount  == 0) {
              throw new RuntimeException("TieredDiscount with ID " + promotionId + " not found.");
              }
       }   

       @Transactional
       public void updateStartDate(UUID promotionId, LocalDate date) {
              String query= "UPDATE TieredPromotion t SET t.startDate = :date WHERE t.promotionId = :promotionId";
              int updatedCount =entityManager.createQuery(query)
                                   .setParameter("date",date)
                                   .setParameter("promotionId", promotionId)
                                   .executeUpdate();

              System.out.println("after execution");
              if (updatedCount  == 0) {
              throw new RuntimeException("TieredDiscount with ID " + promotionId + " not found.");
              }
       }  

              @Transactional
       public void updateEndDate(UUID promotionId, LocalDate date) {
              String query= "UPDATE TieredPromotion t SET t.endDate = :date WHERE t.promotionId = :promotionId";
              int updatedCount =entityManager.createQuery(query)
                                   .setParameter("date",date)
                                   .setParameter("promotionId", promotionId)
                                   .executeUpdate();

              System.out.println("after execution");
              if (updatedCount  == 0) {
              throw new RuntimeException("TieredDiscount with ID " + promotionId + " not found.");
              }
       }  


              @Transactional
       public void updateReductPerc(UUID promotionId, BigDecimal reduction) {
              String query= "UPDATE TieredPromotion t SET t.reductionPercentage = :reduction WHERE t.promotionId = :promotionId";
              int updatedCount =entityManager.createQuery(query)
                                   .setParameter("reduction",reduction)
                                   .setParameter("promotionId", promotionId)
                                   .executeUpdate();

              if (updatedCount  == 0) {
              throw new RuntimeException("TieredDiscount with ID " + promotionId + " not found.");
              }
       }


@Transactional
  public TieredPromotion getCurrentThreshold(){
       String query = "select p from Product p where p.startDate <= current_date and p.endDate >= current_date";    
       TieredPromotion promotion= entityManager.createQuery(query, TieredPromotion.class)
                      .getSingleResult();
     return promotion;
  }
   
  




}