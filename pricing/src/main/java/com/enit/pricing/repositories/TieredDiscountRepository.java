package com.enit.pricing.repositories;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enit.pricing.domain.TieredPromotion;

import jakarta.transaction.Transactional;

@Repository
public interface TieredDiscountRepository  extends JpaRepository<TieredPromotion, UUID>  {

@Query("SELECT p FROM TieredPromotion p " +
       "WHERE p.startDate <= CURRENT_DATE " + 
       "AND p.endDate >= CURRENT_DATE")
    Optional<TieredPromotion> getCurrentTieredDiscount();

    // delete expired discounts
    @Modifying
    @Transactional
    @Query("DELETE FROM TieredPromotion t " +
           "WHERE t.endDate < CURRENT_DATE")
    void deleteExpiredDiscounts();

    // modify reduction percentage
    @Modifying
    @Transactional
    @Query("UPDATE TieredPromotion t " +
           "SET t.reductionPercentage = :newPercentage " +
           "WHERE t.promotionId = :promotionId")
    void updateReductionPercentage(@Param("promotionId") UUID promotionId, @Param("newPercentage") BigDecimal newPercentage
    );

}
