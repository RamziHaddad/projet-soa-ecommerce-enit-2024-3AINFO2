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
import org.ecommerce.domain.Outbox;

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
    @Inject
    OutboxService outboxService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<Product> findByRange(int page, int maxResults) {
        List<Product> products = productRepo.findByRange(page, maxResults);
        updateShownPrices(products);
        return products;
    }

    public List<Product> findAll() {
        List<Product> products = productRepo.findAll();
        updateShownPrices(products);
        return products;
    }

    public Product getProductDetails(UUID id) throws EntityNotFoundException {
        Product product = productRepo.findById(id);
        updateShownPrice(product);
        return product;
    }

    public Product add(Product product, String categoryName) throws EntityAlreadyExistsException, EntityNotFoundException {
        product.setId(UUID.randomUUID());

        double fetchedBasePrice = pricingService.getProductPrice(product.getId());
        product.setBasePrice(new BigDecimal(fetchedBasePrice));
        product.setShownPrice(product.getBasePrice());

        ProductCategory category = categoryService.getCategoryByName(categoryName);
        product.setCategory(category);
        ProductListed productListed = new ProductListed(product);

        Outbox outboxMessage = outboxService.createOutboxMessage(productListed.toString());

        if (outboxMessage == null) {
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message");
        }

        try {
            CompletionStage<Void> ack = productsEmitter.send(productListed);
            ack.thenAccept(result -> {
                logger.info("Product listed and sent via Kafka: " + productListed);
                outboxService.markAsSent(outboxMessage.getId());
            }).exceptionally(error -> {
                logger.error("Error when sending the product listed event: " + error.getMessage());
                outboxService.markAsFailed(outboxMessage.getId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON: " + e.getMessage());
            outboxService.markAsFailed(outboxMessage.getId());
        }

        return productRepo.insert(product);
    }

    public Product updateProduct(Product product) throws EntityNotFoundException {
        return productRepo.update(product);
    }

    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }

    private void updateShownPrices(List<Product> products) {
        for (Product product : products) {
            updateShownPrice(product);
        }
    }

    private void updateShownPrice(Product product) {
        double latestPrice = pricingService.getProductPrice(product.getId());
        product.setShownPrice(new BigDecimal(latestPrice));
    }
}
