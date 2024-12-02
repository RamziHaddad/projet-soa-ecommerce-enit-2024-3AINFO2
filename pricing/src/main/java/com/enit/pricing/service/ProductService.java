package com.enit.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.enit.pricing.domain.Product;
import com.enit.pricing.repositories.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
        private final Map<String, List<BigDecimal>> categoriesRange = new HashMap<>() {{
        put("Electronics", List.of(new BigDecimal("12.56"), new BigDecimal("999.99")));
        put("Clothing", List.of(new BigDecimal("100.00"), new BigDecimal("200.00")));
        put("Books", List.of(new BigDecimal("5.00"), new BigDecimal("50.00")));
        put("Home Appliances", List.of(new BigDecimal("300.00"), new BigDecimal("1000.00")));
    }};

    
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductBasePrice(UUID productId, BigDecimal newBasePrice) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setBasePrice(newBasePrice);
        return productRepository.save(product); 
    }   


    /*   
    Once a product (product UUID + category ) is received from the inventory 
    its price will be set randomly within a certain range based on the category 
    */

    public static BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
        Random random = new Random();
        double randomValue = min.doubleValue() + (max.doubleValue() - min.doubleValue()) * random.nextDouble();
                BigDecimal randomBigDecimal = new BigDecimal(randomValue);
                return randomBigDecimal.setScale(3, RoundingMode.HALF_UP);
    }


    // this function will set random price value in a certain range based the category of the product. 
    // for each category a range is defined

    public BigDecimal setProdBasePriceCatgRandomly(String category){
        BigDecimal price=BigDecimal.ZERO;
        List<BigDecimal> range = categoriesRange.get(category);
        price = getRandomBigDecimal(range.get(0), range.get(1));
        return price;
    }

    // add new product(sent by inventory) to the database
    public void addProduct(UUID productId, String category){
        BigDecimal price= setProdBasePriceCatgRandomly(category);
        Product product = new Product(productId,price,category);
        productRepository.save(product);
    }

    public void updateProduct(UUID productId, String category){
        Product product = getProduct(productId);
        if(product==null){
            addProduct(productId, category);
        }else {
            //productRepository.updateProdCategory(productId, category);
        } 
    }
    
    public void deleteProduct(UUID productId){
        Product product = getProduct(productId);
        if(product!=null){
            productRepository.deleteById(productId);
        }
    }



    
    
}



