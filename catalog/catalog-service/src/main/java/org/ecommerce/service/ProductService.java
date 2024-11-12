package org.ecommerce.service;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.ecommerce.domain.Product;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepo;

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
        return productRepo.addProductWithCategory(product, categoryName);
    }

    public Product updateProduct(Product product) throws EntityNotFoundException {
        return productRepo.update(product);      
    }

    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }
}
