package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductCategoryRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductCategoryService {

    @Inject
    ProductCategoryRepository categoryRepo;

    @Transactional
    public ProductCategory addCategory(ProductCategory category) throws EntityAlreadyExistsException {

        try {
            categoryRepo.findByName(category.getCategoryName());
            throw new EntityAlreadyExistsException("Category with name " + category.getCategoryName() + " already exists.");
        } catch (EntityNotFoundException e) {

            return categoryRepo.insert(category);
        }
    }

    @Transactional
    public ProductCategory updateCategory(ProductCategory category) throws EntityNotFoundException {

        ProductCategory existingCategory = categoryRepo.findById(category.getId());
        if (existingCategory == null) {
            throw new EntityNotFoundException("Category not found for ID: " + category.getId());
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

    @Transactional
    public ProductCategory getCategoryById(UUID id) throws EntityNotFoundException {

        return categoryRepo.findById(id);
    }

    @Transactional
    public ProductCategory getCategoryByName(String categoryName) throws EntityNotFoundException {

        return categoryRepo.findByName(categoryName);
    }

    @Transactional
    public List<ProductCategory> getAllCategories() {

        return categoryRepo.findAll();
    }
}
