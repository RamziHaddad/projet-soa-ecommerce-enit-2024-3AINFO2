package enit.ecomerce.search_product.service;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProductRepository;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InboxProductCreationService {
    @Autowired
    private ProducteEntityRepository productEntityRepository; 
    @Autowired 
    private ProductRepository productRepository; 
    @Scheduled(fixedRate = 300000)  

    public void treatInbox() {
       
        List<ProductEntity> unindexedProducts = productEntityRepository.findUnindexedProducts();
 
        for (ProductEntity product : unindexedProducts) {
            try {
                //we should see how to make these operation a transaction , 
                //i dont want to save a product annd not set it in the inbox
                productRepository.save(new Product(product));

              
                product.setIndex(true);
                productEntityRepository.save(product);

            } catch (Exception e) {
                System.err.println("Error processing product: " + product.getId() + " - " + e.getMessage());
            }
        }
    }
} 
           

