package com.enit.pricing.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;


@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, UUID> {


    long count();

    //void deletePromotionByProductId(UUID productId);

/*    @Query("SELECT p FROM ProductPromotion p " +
       "WHERE p.startDate <= CURRENT_DATE " +
       "AND p.endDate >= CURRENT_DATE " +
       "AND p.product.productId = :prodId")
   List<ProductPromotion> findActivePromotions(@Param("prodId") UUID prodId); */


 
   @Query("SELECT p FROM ProductPromotion p " +
        "WHERE p.product.productId = :prodId "+
           "AND p.startDate <= :currentDate " +
           "AND p.endDate >= :currentDate")
   Optional<ProductPromotion> getPromotionByProduct(@Param("prodId") UUID prodId);
     

   @SuppressWarnings({ "null", "unchecked" })
   ProductPromotion save(ProductPromotion productPromotion);
   void deleteById(@SuppressWarnings("null") UUID promotionId);

   @Modifying
   @Query("DELETE FROM ProductPromotion p WHERE p.product.productId = :prodId")
   void deletePromotionsByProduct(@Param("prodId") UUID prodId);

   @Modifying
   @Query("UPDATE ProductPromotion p SET p.product = :product WHERE p.promotionId = :promotionId")
   void addProductToPromotion(@Param("promotionId") UUID promotionId, @Param("product") Product product);

   @Modifying
   @Query("DELETE FROM ProductPromotion p WHERE p.endDate < CURRENT_DATE")
   void deleteExpiredPromotions();


   
    
    }
