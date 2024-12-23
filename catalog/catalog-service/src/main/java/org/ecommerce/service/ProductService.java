package org.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.domain.events.ProductUpdated;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepo;

    @Inject
    ProductCategoryService categoryService;

    @RestClient
    PricingService pricingService;
    
    @Inject
    OutboxService outboxService;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public List<Product> findByRange(int page, int maxResults) {
        List<Product> products = productRepo.findByRange(page, maxResults);
        products.forEach(product -> {
            BigDecimal price = pricingService.getProductPrice(product.getId());
            product.setShownPrice(price);
        });
        return products;
    }

    @Transactional
    public List<Product> findAll() {
        List<Product> products = productRepo.findAll();
        products.forEach(product -> {
            BigDecimal price = pricingService.getProductPrice(product.getId());
            product.setShownPrice(price);
        });
        return products;
    }

    @Transactional
    public Product getProductDetails(UUID id) throws EntityNotFoundException {
        Product product = productRepo.findById(id);
        BigDecimal price = pricingService.getProductPrice(product.getId());
        product.setShownPrice(price);
        return product;
    }
    @Transactional
    public Product add(Product product, String categoryName) throws EntityAlreadyExistsException, EntityNotFoundException {
        product.setId(UUID.randomUUID());


        BigDecimal price = pricingService.getProductPrice(product.getId());
        product.setBasePrice(price);
        product.setShownPrice(price);

        ProductCategory category = categoryService.getCategoryByName(categoryName);
        product.setCategory(category);

        ProductListed productListed = new ProductListed(product);

        try {
            outboxService.createOutboxMessage(productListed);
        } catch (Exception e) {
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message");
        }

        return productRepo.insert(product);
    }

    @Transactional
    public Product updateProduct(Product product) throws EntityNotFoundException {
        Product existingProduct = productRepo.findById(product.getId());

        if (product.getCategory() != null) {
            ProductCategory newCategory = categoryService.getCategoryByName(product.getCategory().getCategoryName());
            if (newCategory != null) {
                existingProduct.setCategory(newCategory);
            } else {
                throw new EntityNotFoundException("Category not found: " + product.getCategory().getCategoryName());
            }
        }
        if (product.getProductName() != null) {
            existingProduct.setProductName(product.getProductName());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getShownPrice() != null) {
            existingProduct.setShownPrice(product.getShownPrice());
        }
        existingProduct.setDisponibility(product.isDisponibility());

        ProductUpdated productUpdated = new ProductUpdated(existingProduct);
        try {
            outboxService.createOutboxMessage(productUpdated);
        } catch (JsonProcessingException e) {
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message");
        } catch (EntityAlreadyExistsException e) {
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message");
        }

        return productRepo.update(existingProduct);
    }

    @Transactional
    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }
}
