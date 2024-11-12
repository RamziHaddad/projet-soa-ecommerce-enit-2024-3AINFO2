package com.enit.pricing.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enit.pricing.domain.Promotion;


@Repository
public interface promotionRepository extends JpaRepository<Promotion, UUID> {


    void deleteById(int promotionId);
    List<Promotion> findAll();
    long count();

    void deletePromotionByProductId(UUID productId);

    @Query("SELECT p FROM Promotion p WHERE  (p.startDate <= :endDate AND p.endDate >= :startDate)")
    List<Promotion> findActivePromotions(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    }
