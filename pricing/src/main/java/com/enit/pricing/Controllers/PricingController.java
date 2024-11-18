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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;




@SpringBootApplication
@RestController
@RequestMapping("/pricing")
public class PricingController {

    private final ProductService productService;
    private final PricingService pricingService;

    @Autowired
    public PricingController(ProductService productService, PricingService pricingService) {

        this.productService = productService;
        this.pricingService = pricingService;
    }

    // Calculate the cart total
    @PostMapping("/cart-total")
    public ResponseEntity <CartResponse> calculateCartTotal(@RequestBody List<CartItem> prodId) {
        BigDecimal totalBefore= pricingService.calculateCartTotal(prodId);
        BigDecimal total=pricingService.calculateCartTotalFinal(prodId);
        CartResponse cartResponse = new CartResponse(totalBefore, total);
        return ResponseEntity.ok(cartResponse);
    }

    // Calculate product price after promotion
    @PostMapping("/product-price")
    public ResponseEntity<CatalogPriceResponse> calculateProductPrice(@RequestBody UUID prodId) {
        BigDecimal price = pricingService.calculateProductPrice(prodId);
        CatalogPriceResponse catalogPriceResponse = new CatalogPriceResponse(prodId, price);
        return ResponseEntity.ok(catalogPriceResponse);
    }

    // Returns the base price of a product
    @PostMapping("/base-price")
    public ResponseEntity<BigDecimal> returnBasePrice(@RequestBody UUID prodId) {
        BigDecimal basePrice = productService.getProductBasePrice(prodId);
        return ResponseEntity.ok(basePrice);
    }

}
