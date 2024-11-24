package com.enit.pricing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.enit.pricing.domain.Product;
import com.enit.pricing.repositories.ProductRepository;

public class ProductService {
    private final ProductRepository productRepository;
    
    @Autowired
    public  ProductService(ProductRepository productRepository){
        this.productRepository= productRepository;
    }

    public BigDecimal getProductBasePrice(UUID productId) {
            return productRepository.findBasePriceByProductId(productId)
                .orElse(null);
        }
    public Product getProduct(UUID productId){
        Optional<Product> product= productRepository.findById(productId);
        return product.orElse(null);
    }

/*     public boolean updateProductPrice(UUID productId, BigDecimal newPrice) {
        return productRepository.updateProductBasePrice(productId, newPrice) > 0;
    } */

/*     public Product addProduct(Product product) {
        return productRepository.save(product);
    }
        public Product addProduct2(BigDecimal basePrice) {
        Product product = new Product();
        product.setBasePrice(basePrice);
        return productRepository.save(product);
    } */

        public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Product updateProductBasePrice(UUID productId, BigDecimal newBasePrice) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    product.setBasePrice(newBasePrice);
    return productRepository.save(product); 
}

    // this function will set random price value in a certain range based the category of the product. 
    // for each category a range is defined
    public BigDecimal setProductBasePriceCategoryRandomly(String string){
        BigDecimal price=BigDecimal.ZERO;

        return price;
    }

    // add product to the database
    public void addProduct(UUID product, String category){
        setProductBasePriceCategoryRandomly(category);
    }

    public void updateProduct(UUID productId, String category){

    }
    public void deleteProduct(UUID productId){
        
    }



    
    
}



