package org.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.domain.events.Event;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;

//import io.quarkus.scheduler.Scheduled;
//import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepo;
    @Inject
    ProductCategoryService categoryService;
    @RestClient
    PricingService pricingService;
    @Inject
    @Channel("products-out")
    Emitter<Event> productsEmitter;
    private final Logger logger= LoggerFactory.getLogger(ProductService.class);

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
        product.setId(UUID.randomUUID());

        //waiting for pricing 
        //product.setBasePrice(pricingService.getProductPrice(product.getId()));
        BigDecimal temp = new BigDecimal("10506");
        product.setBasePrice(temp);
        product.setShownPrice(product.getBasePrice());
        /// temporary for testing 
        
        ProductCategory category = categoryService.getCategoryByName(categoryName);
        product.setCategory(category);
        ProductListed productListed = new ProductListed(product);
        try {
            CompletionStage<Void> ack = productsEmitter.send(productListed);
            ack.thenAccept(result -> {
                logger.info("Product listed and sent via Kafka "+ack );
            }).exceptionally(error -> {
                logger.error("Error when sending the productlisted event");
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when Serializing JSON ");
        }
        System.out.println(productListed);
        return productRepo.insert(product);
    }

    // @Scheduled(every = "12h", concurrentExecution = ConcurrentExecution.SKIP)
    // public void checkPriceUpdates(){
    //     int page = 0;
    //     int maxResults = 10; // Adjust the range as needed
    //     List<Product> products;
    //     do {
    //         products = findByRange(page, maxResults);
    //         products.forEach(product -> {
    //             BigDecimal newPrice = pricingService.getProductPrice(product.getId());
    //             if (product.getShownPrice() != newPrice) {
    //                 product.setShownPrice(newPrice);
    //                 try {
    //                     productRepo.update(product);
    //                 } catch (EntityNotFoundException e) {
    //                     e.printStackTrace();
    //                 }
    //             }
    //         });
    //         page++;
    //     } while (!products.isEmpty());
    // }

    public Product updateProduct(Product product) throws EntityNotFoundException {
        return productRepo.update(product);      
    }

    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }
}
