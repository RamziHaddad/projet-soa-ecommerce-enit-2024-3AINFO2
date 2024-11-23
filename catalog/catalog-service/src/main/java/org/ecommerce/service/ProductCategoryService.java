package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductCategoryRepository;

import java.util.UUID;

@ApplicationScoped
public class ProductCategoryService {

    @Inject
    ProductCategoryRepository categoryRepo;

    @Transactional
    public ProductCategory addCategory(ProductCategory category) throws EntityAlreadyExistsException {
            return categoryRepo.insert(category);
    }

    @Transactional
    public ProductCategory updateCategory(ProductCategory category) throws EntityNotFoundException {
        ProductCategory existingCategory = categoryRepo.findByName(category.getCategoryName());
        if (existingCategory == null) {
            throw new EntityNotFoundException("Category with name " + category.getCategoryName() + " not found");
        }
        return categoryRepo.update(category);
    }

    @Transactional
    public void removeCategory(UUID id) throws EntityNotFoundException {
        ProductCategory category = categoryRepo.findById(id);
        if (category == null) {
            throw new EntityNotFoundException("Category not found for ID: " + id);
        }
        categoryRepo.delete(category);
    }

    public ProductCategory getCategoryByName(String categoryName) throws EntityNotFoundException {
        return categoryRepo.findByName(categoryName);
    }
}
