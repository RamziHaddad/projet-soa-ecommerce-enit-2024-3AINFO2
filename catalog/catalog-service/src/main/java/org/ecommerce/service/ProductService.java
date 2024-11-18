package org.ecommerce.service;

import java.util.List;
import java.util.UUID;

import org.ecommerce.domain.Product;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepo;
    @Inject
    PricingService pricingService;

    public List<Product> findByRange(int page , int maxResults) {
        return productRepo.findByRange(page , maxResults);
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product getProductDetails(UUID id) throws EntityNotFoundException {
        return productRepo.findById(id);
    }

    public Product add(Product product, String categoryName) throws EntityAlreadyExistsException, EntityNotFoundException {
        product.setBasePrice(pricingService.getProductPrice(product.getId()));
        product.setShownPrice(product.getBasePrice());
        return productRepo.addProductWithCategory(product, categoryName);
    }

    @Scheduled(every = "12h", concurrentExecution = ConcurrentExecution.SKIP)
    public void checkPriceUpdates(){
        int page = 0;
        int maxResults = 10; // Adjust the range as needed

        List<Product> products;
        do {
            products = findByRange(page, maxResults);
            products.forEach(product -> {
                double newPrice = pricingService.getProductPrice(product.getId());
                if (product.getShownPrice() != newPrice) {
                    product.setShownPrice(newPrice);
                    try {
                        productRepo.update(product);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            page++;
        } while (!products.isEmpty());
    }

    public Product updateProduct(Product product) throws EntityNotFoundException {
        return productRepo.update(product);      
    }

    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }
}
