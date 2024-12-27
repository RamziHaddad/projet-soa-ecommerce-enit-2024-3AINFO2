package com.enit.pricing.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Repository
public class ProductPromotionRepository {

    @Autowired
    private EntityManager entityManager;


 
    @Transactional
    public void addPromotion(ProductPromotion promotion) {
        if (promotion.getPromotionId() != null && entityManager.find(Product.class, promotion.getPromotionId()) != null) {
            throw new IllegalStateException("Product already exists with ID: " + promotion.getPromotionId());
        } 
        entityManager.persist(promotion);
    }

    @Transactional
    public ProductPromotion getPromotionByProduct(UUID prodId) {
        try {
            String query = "SELECT pp FROM ProductPromotion pp JOIN pp.products p " +
                          "WHERE p.productId = :prodId " +
                          "AND pp.startDate <= CURRENT_DATE " +
                          "AND pp.endDate >= CURRENT_DATE";
            return entityManager.createQuery(query, ProductPromotion.class)
                              .setParameter("prodId", prodId)
                              .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No active promotion found for product: " + prodId);
        } catch (PersistenceException e) {
            throw new RuntimeException("Error fetching promotion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePromotionById(UUID promoId) {
        ProductPromotion promotion = findById(promoId);
        if (promotion == null) {
            throw new EntityNotFoundException("Promotion not found with ID: " + promoId);
        }
        try {
            promotion.getProducts().forEach(product -> product.setPromotion(null));
            entityManager.remove(promotion);
        } catch (PersistenceException e) {
            throw new RuntimeException("Failed to delete promotion: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ProductPromotion findById(UUID promoId) {
        try {
            ProductPromotion promotion = entityManager.find(ProductPromotion.class, promoId);
            if (promotion == null) {
                throw new EntityNotFoundException("Promotion not found with ID: " + promoId);
            }
            return promotion;
        } catch (PersistenceException e) {
            throw new RuntimeException("Error fetching promotion: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void deleteExpiredPromotions() {
        try{
            String query = "select p from ProductPromotion p where p.endDate < CURRENT_DATE";
            List<ProductPromotion> promotions = entityManager.createQuery(query, ProductPromotion.class).getResultList();
            if (promotions.isEmpty()) {
                throw new EntityNotFoundException("No expired promotions found");
            }
            promotions.forEach(promotion -> deletePromotionById(promotion.getPromotionId()));
        }catch(PersistenceException e){
            throw new RuntimeException("Error fetching promotions: " + e.getMessage(), e);
        }

    }


    @Transactional
    public void updateStartDate(UUID promotionId, LocalDate date) {
        try {
            String query = "UPDATE ProductPromotion p SET p.startDate = :date WHERE p.promotionId = :promotionId";
            int updated = entityManager.createQuery(query)
                    .setParameter("date", date)
                    .setParameter("promotionId", promotionId)
                    .executeUpdate();
            if (updated == 0) {
                throw new EntityNotFoundException("Promotion not found with ID: " + promotionId);
            }
        } catch (PersistenceException e) {
            throw new RuntimeException("Failed to update start date: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateEndDate(UUID promotionId, LocalDate date) {
        try{
            String query = "UPDATE ProductPromotion p SET p.endDate = :date WHERE p.promotionId = :promotionId";
            int updated = entityManager.createQuery(query)
                    .setParameter("date", date)
                    .setParameter("promotionId", promotionId)
                    .executeUpdate();

            if (updated == 0) {
                throw new RuntimeException("Promotion with ID " + promotionId + " not found.");
            }
        }catch(PersistenceException e){
            throw new RuntimeException("Failed to update end date: " + e.getMessage(), e);
        }

    }

    public List<ProductPromotion> getActivePromotions() {
        try{
            String query = "select p from ProductPromotion p where p.startDate <= CURRENT_DATE and p.endDate >= CURRENT_DATE";
            List<ProductPromotion> promotions = entityManager.createQuery(query, ProductPromotion.class).getResultList();
            if(promotions.isEmpty()){
                throw new RuntimeException("No active promotions found");
            }
            return promotions;

        }catch(PersistenceException e){
            throw new RuntimeException("Failed to fetch promotions: "+ e.getMessage(),e);
        }
    }

    public List<ProductPromotion> getExpiredPromotions() {
        try{
            String query = "select p from ProductPromotion p where p.endDate <= CURRENT_DATE";
            List<ProductPromotion> promotions = entityManager.createQuery(query, ProductPromotion.class).getResultList();
            if(promotions.isEmpty()){
                throw new RuntimeException("No expired promotions found");
            }
            return promotions;

        }catch(PersistenceException e){
            throw new RuntimeException("Failed to fetch promotions: "+ e.getMessage(),e);
        }
    }

    @Transactional
    public void updateReductPerc(UUID promotionId, BigDecimal reduction) {
            try{
                String query= "update ProductPromotion p SET p.reductionPercentage = :reduction where p.promotionId = :promotionId";
                int updatedCount =entityManager.createQuery(query)
                                    .setParameter("reduction",reduction)
                                    .setParameter("promotionId", promotionId)
                                    .executeUpdate();

                if (updatedCount  == 0) {
                throw new RuntimeException("ProductPromotion with ID " + promotionId + " not found.");
                }
            }catch(PersistenceException e){
                throw new RuntimeException("Failed to update reduction percentage");
            }
    }

    public List<Product> getProducts(UUID promotionId) {
        try{
            String query = "select p FROM Product p where p.promotion.id = :promotionId";
            List<Product> products = entityManager.createQuery(query, Product.class)
                                                    .setParameter("promotionId", promotionId)
                                                    .getResultList()  ;
            if(products.isEmpty()){
                throw new RuntimeException("No promotions for this product");
            }
            return products;
        }catch(PersistenceException e){
            throw new RuntimeException("Error fetching products");
        }
    }
    
}
