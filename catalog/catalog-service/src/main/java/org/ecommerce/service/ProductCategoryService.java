package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.repository.ProductCategoryRepository;

@ApplicationScoped
public class ProductCategoryService {

    @Inject
    ProductCategoryRepository categoryRepo;

    @Transactional
    public ProductCategory addCategory(ProductCategory category) throws EntityAlreadyExistsException {
        return categoryRepo.insert(category);
    }
}
