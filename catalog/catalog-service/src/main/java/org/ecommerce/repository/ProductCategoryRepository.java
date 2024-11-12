package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.UUID;

import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityNotFoundException;

@ApplicationScoped
public class ProductCategoryRepository {

    @Inject
    EntityManager em;

    public ProductCategory findByName(String categoryName) throws EntityNotFoundException {
        try {
            return em.createQuery("SELECT c FROM ProductCategory c WHERE c.categoryName = :categoryName", ProductCategory.class)
                     .setParameter("categoryName", categoryName)
                     .getSingleResult();
        } catch (Exception e) {
            throw new EntityNotFoundException("Cannot find category with name: " + categoryName);
        }
    }

    @Transactional
    public ProductCategory insert(ProductCategory category) {
            category.setId(UUID.randomUUID());
        em.persist(category);
        return category;
    }
}
