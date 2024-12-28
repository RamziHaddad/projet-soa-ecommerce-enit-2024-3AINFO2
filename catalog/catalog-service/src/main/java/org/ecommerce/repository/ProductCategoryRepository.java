package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
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

    public ProductCategory findById(UUID id) throws EntityNotFoundException {
        ProductCategory category = em.find(ProductCategory.class, id);
        if (category == null) {
            throw new EntityNotFoundException("Category not found for ID: " + id);
        }
        return category;
    }

    @Transactional
    public ProductCategory insert(ProductCategory category) {
        category.setId(UUID.randomUUID());
        em.persist(category);
        return category;
    }

    @Transactional
    public ProductCategory update(ProductCategory category) {
        return em.merge(category);
    }

    @Transactional
    public void delete(ProductCategory category) {
        em.remove(em.contains(category) ? category : em.merge(category));
    }

    public List<ProductCategory> findAll() {
        return em.createQuery("SELECT c FROM ProductCategory c", ProductCategory.class).getResultList();
    }
}
