package com.enit.pricing.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    // Update base price
    /* @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.basePrice = :newBasePrice WHERE p.productId = :productId")
    int  updateProductBasePrice(@Param("productId") UUID productId, @Param("newBasePrice") BigDecimal newBasePrice);
 */


}