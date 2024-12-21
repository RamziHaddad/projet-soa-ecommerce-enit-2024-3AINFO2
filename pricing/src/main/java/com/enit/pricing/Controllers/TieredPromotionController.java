package com.enit.pricing.Controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enit.pricing.domain.TieredPromotion;
import com.enit.pricing.dto.ReducThreshold;
import com.enit.pricing.dto.UpdateDateDto;
import com.enit.pricing.service.TieredDsicountService;

import jakarta.persistence.EntityNotFoundException;



@SpringBootApplication
@RestController
@RequestMapping("/tieredPromotion")
public class TieredPromotionController {
    
    @Autowired 
    TieredDsicountService tieredDsicountService;



    @PostMapping("/addPromotion")
    public ResponseEntity<TieredPromotion> addPromotion(@RequestBody TieredPromotion promotion) {
            tieredDsicountService.addProductPromotion(promotion);
            return ResponseEntity.status(HttpStatus.CREATED).body(promotion);
       
    }

    @DeleteMapping("/deletePromotion/{promotionId}")
    public ResponseEntity<String> deletePromotion(@PathVariable UUID promotionId){
        try{
            tieredDsicountService.deletePromotion(promotionId);
            return ResponseEntity.ok("Promotion with ID "+promotionId+ " deleted");
            }catch(EntityNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion with ID "+promotionId+ " not found");
            }       
    }


    @GetMapping("/getPromotion/{promotionId}")
    public ResponseEntity<TieredPromotion> getPromotionById(@PathVariable UUID promotionId) {
        try{
            TieredPromotion promotion= tieredDsicountService.getPromotionById(promotionId);
            return ResponseEntity.ok()
                                 .header("message","Product updated")
                                 .body(promotion);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .header("message","promotion for product "+ promotionId +" not found")
                                 .build();
        }
    }


    @PutMapping("/updateThreshold")
    public ResponseEntity<String> updateThreashold(@RequestBody ReducThreshold value) {
        try {
            tieredDsicountService.updateThreashold(value.getPromotionId(), value.getReducThreshold());
            return ResponseEntity.ok("Threashold percentage updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Threashold percentage: " + e.getMessage());
        }
    }

    @PutMapping("/startDate")
    public ResponseEntity<String> updateStartDate(@RequestBody UpdateDateDto date) {
        try {
            tieredDsicountService.updateStartDate(date.getPromotionId(), date.getDate());
            return ResponseEntity.ok("startDate percentage updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating startDate percentage: " + e.getMessage());
        }
    }

    @PutMapping("/endDate")
    public ResponseEntity<String> updateEndDate(@RequestBody UpdateDateDto date) {
        try {
            tieredDsicountService.updateEndDate(date.getPromotionId(), date.getDate());
            return ResponseEntity.ok("endDate percentage updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating endDate percentage: " + e.getMessage());
        }
    }


    @PutMapping("/updateReductionPer")
    public ResponseEntity<String> updateReductPerc(@RequestBody ReducThreshold value) {
        try {
            tieredDsicountService.updateReductPerc(value.getPromotionId(), value.getReducThreshold());
            return ResponseEntity.ok("Reduction percentage updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error updating Reduction percentage: " + e.getMessage());
        }
    }


    @GetMapping("currentPromotion")
    public TieredPromotion getCurrentPromotion() {
        return tieredDsicountService.getCurrentThrehold();
    }
    
    }




    
    

    

