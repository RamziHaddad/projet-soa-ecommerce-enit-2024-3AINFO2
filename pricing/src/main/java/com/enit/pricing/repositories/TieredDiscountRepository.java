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

   // @Query("SELECT p.thresholdAmount from  TieredDiscount p where ( currDate >= p.startDate AND currDate <= p.endDate) ")
    @Query("SELECT p FROM TieredDiscount p "+
    "WHERE  p.startDate <= CURRENT_DATE "  + 
     "AND p.endDate >= CURRENT_DATE " 
    )
    Optional<TieredPromotion> getCurrentDiscount();

    // delete expired discounts
    @Modifying
    @Transactional
    @Query("DELETE FROM TieredDiscount t " +
           "WHERE t.endDate < CURRENT_DATE")
    void deleteExpiredDiscounts();

    // modify reduction percentage
    @Modifying
    @Transactional
    @Query("UPDATE TieredDiscount t " +
           "SET t.reductionPercentage = :newPercentage " +
           "WHERE t.tieredDiscountId = :promotionId")
    void updateReductionPercentage(@Param("promotionId") UUID promotionId, @Param("newPercentage") BigDecimal newPercentage
    );

}
