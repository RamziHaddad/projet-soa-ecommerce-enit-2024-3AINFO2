package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ecommerce.model.Category;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CategoryRepository {
    @Inject
    EntityManager em;

    @Transactional
    public List<Category> listAll() {
        return em.createQuery("from Category t ", Category.class).getResultList();
    }

    @Transactional
    public Optional<Category> getCategoryByID(UUID id) {
        Category category = em.find(Category.class, id);
        return Optional.ofNullable(category);
    }

    @Transactional
    public Category addCategory(Category category) {
        em.persist(category);
        return category;
    }

    @Transactional
    public Category updateCategory(Category category) {
        return em.merge(category);
    }

    @Transactional
    public void deleteCategoryById(UUID id) {
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
        }
    }

}
