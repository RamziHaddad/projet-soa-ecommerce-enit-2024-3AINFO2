package com.enit.pricing.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;
import com.enit.pricing.repositories.ProductPromotionRepository;
import com.enit.pricing.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductPromotionRepository productPromotionRepository;




    public BigDecimal getBasePrice(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        return productRepository.getBasePrice(productId);
    }

    public BigDecimal getCurrentPrice(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        return productRepository.getCurrentPrice(productId);
    }


    public Product getProduct(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
        return product;
    }


    public void updateProductCurrentPrice(UUID productId, BigDecimal price) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
        product.setCurrentPrice(price);
        productRepository.updateProduct(product);
    }   

    
    public void updateBasePrice(UUID productId, BigDecimal price) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        productRepository.setBasePrice(productId, price);
    }


    // add new product(sent by inventory) to the database only the id will be added to the db the price will be set later
    public Product addProduct(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        Product product = new Product(productId);
        productRepository.addProduct(product);
        return product;
    }

    public Product updateProduct(UUID prodId) {
        Product product=productRepository.findById(prodId);
        if (product == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        if (product.getProductId() == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        //productRepository.findById(product.getProductId());
        productRepository.updateProduct(product);
        return product;
    }
    

    public void deleteProduct(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productRepository.findById(productId) == null) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteProduct(productId);
    }



    public void addPromotiontoProduct(UUID productId, UUID promotionId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (promotionId == null) {
            throw new IllegalArgumentException("Promotion ID cannot be null");
        }
        Product product = getProduct(productId); // It will throw EntityNotFoundException if not found
        ProductPromotion promotion = productPromotionRepository.findById(promotionId);
        if (promotion == null) {
            throw new EntityNotFoundException("Promotion not found with ID: " + promotionId);
        }

        productRepository.addPromotiontoProduct(productId, promotion);
    }

    public void removePromotionFromProduct(UUID productId){
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productRepository.findById(productId) == null) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.removePromotionFromProduct(productId);
    }
 
    
}



