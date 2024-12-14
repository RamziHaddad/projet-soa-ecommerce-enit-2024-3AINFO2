package enit.ecomerce.search_product.service;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProductRepository;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InboxProductCreationService {
    @Autowired
    private ProducteEntityRepository productEntityRepository;
    @Autowired
    private ProductRepository productRepository;

    private static final int BATCH_SIZE = 50; // Define your batch size

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void treatInbox() {
        List<ProductEntity> unindexedProducts;

        while (!(unindexedProducts = productEntityRepository.findUnindexedProducts(BATCH_SIZE)).isEmpty()) {
            for (ProductEntity product : unindexedProducts) {
                try {
                    productRepository.save(new Product(product));
                    product.setIndex(true);
                    productEntityRepository.save(product);
                } catch (Exception e) {
                    System.err.println("Error processing product: " + product.getId() + " - " + e.getMessage());
                    throw e; // Rethrow the exception to trigger transaction rollback
                }
            }
        }
    }
}
