package com.enit.pricing.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.enit.pricing.dto.CartItem;
import com.enit.pricing.dto.CatalogPriceResponse;
import com.enit.pricing.dto.CartResponse;
import com.enit.pricing.service.PricingService;
import com.enit.pricing.service.ProductService;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@SpringBootApplication
@RestController
@RequestMapping("/pricing")
public class PricingController {

    private final ProductService productService;
    private final PricingService pricingService;

    public PricingController(ProductService productService, PricingService pricingService) {

        this.productService = productService;
        this.pricingService = pricingService;
    }

    // Calculate the cart total
    @PostMapping("/cart-total")
    public ResponseEntity <CartResponse> calculateCartTotal(@RequestBody List<CartItem> cartItems) {
        BigDecimal totalBefore= pricingService.calculateCartTotal(cartItems);
        BigDecimal total=pricingService.calculateCartTotalFinal(cartItems);
        CartResponse cartResponse = new CartResponse(totalBefore, total);
        return ResponseEntity.ok(cartResponse);
    }

//Calculate product price after promotion
    @PostMapping("/product-price")
    public ResponseEntity<CatalogPriceResponse> calculateProductPrice(@RequestBody UUID prodId) {
        //BigDecimal price = pricingService.calculateProductPrice(prodId);
        System.out.println("im in");
        BigDecimal price = BigDecimal.valueOf(19.99);
        CatalogPriceResponse catalogPriceResponse = new CatalogPriceResponse(prodId, price);
        return ResponseEntity.ok(catalogPriceResponse);
    } 


    // Returns the base price of a product
    @GetMapping("/base-price/{productId}")
    public ResponseEntity<BigDecimal> returnBasePrice(@RequestParam UUID productId) {
        BigDecimal basePrice = productService.getProductBasePrice(productId);
        return ResponseEntity.ok(basePrice);
    }
    

}
