package com.enit.pricing.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.promotion;


@Repository
public interface promotionRepository extends JpaRepository<promotion, Integer> {


    void deleteById(int promotionId);
    List<promotion> findAll();
    long count();

    List<promotion> findByProductId(int productId);
    void deletePromotionByProductId(int productId);

    @Query("SELECT p FROM Promotion p WHERE  (p.startDate <= :endDate AND p.endDate >= :startDate)")
    List<promotion> findActivePromotions(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    }
