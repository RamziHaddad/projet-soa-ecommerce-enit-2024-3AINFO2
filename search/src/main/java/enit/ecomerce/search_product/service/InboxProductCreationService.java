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
    private static final int BATCH_SIZE = 100;

    @Autowired
    private ProducteEntityRepository productEntityRepository;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void treatInbox() {
        List<ProductEntity> unindexedProducts;

        do {
            // Retrieve unindexed products in batches
            unindexedProducts = productEntityRepository.findUnindexedProducts(BATCH_SIZE);

            if (!unindexedProducts.isEmpty()) {
                try {
                    batchIndexProducts(unindexedProducts);
                } catch (Exception e) {
                    System.err.println("Error processing batch: " + e.getMessage());
                    throw e; // Rethrow to trigger rollback
                }
            }
        } while (!unindexedProducts.isEmpty());
    }

    private void batchIndexProducts(List<ProductEntity> unindexedProducts) {
        for (ProductEntity product : unindexedProducts) {
            productRepository.save(new Product(product));
            product.setIndex(true);
            productEntityRepository.save(product);
        }
    }
}
