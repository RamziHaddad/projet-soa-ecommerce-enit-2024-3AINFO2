package com.enit.pricing.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.enit.pricing.domain.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    


    Optional<BigDecimal> findBasePriceByProductId(UUID productId);


    @Query("SELECT p from Product p WHERE p.productId= :productId")
    Optional<Product> getProductByProductId(@Param("productId") UUID productId);
/*
   @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.category = :category WHERE p.productId = :productId")
    void updateProdCategory(@Param("productId") UUID productId, @Param("category") String category);
    

     @Query("delete from Product p where p.productId= :prodId")
    void deleteProduct(@Param("prodId") UUID prodId); */


}