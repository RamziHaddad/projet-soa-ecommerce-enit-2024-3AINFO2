package com.enit.pricing.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.Promotion;


public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product getById(UUID productId); 


    @Query("SELECT p.category FROM Product p WHERE p.productId = :productId")
    String getCategoryById(@Param("productId") UUID productId);

    @Query("Select p.basePrice FROM Product p WHERE p.productId=: productId")
    double getBasePriceById(@Param("productId") UUID proUuid);

    @Query("SELECT p.promotions FROM Product p WHERE p.productId = :productId")
    List<Promotion> getProductPromotion(@Param("productId") UUID productId);

    @Query("SELECT p.promotions FROM Product p " +
           "JOIN p.promotions pr " +
           "WHERE p.productId = :productId " +
           "AND pr.startDate <= CURRENT_DATE " +
           "AND pr.endDate >= CURRENT_DATE")
    List<Promotion> findActivePromotionsForProduct(@Param("productId") UUID productId);

} 