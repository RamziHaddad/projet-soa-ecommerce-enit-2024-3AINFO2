package org.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;

import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.exceptions.EntityAlreadyExistsException;

@ApplicationScoped
public class ProductRepository {

    @Inject
    EntityManager em;
    @Inject
    ProductCategoryRepository categoryRepo;

    public List<Product> findAll() {
        return em.createQuery("from Product", Product.class)
                 .getResultList();
    }

    public List<Product> findByRange(int offset, int range) {
        return em.createQuery("from Product", Product.class)
                 .setFirstResult(offset)
                 .setMaxResults(range)
                 .getResultList();
    }
    

    public Product findById(UUID id) throws EntityNotFoundException {
        Product product = em.find(Product.class, id);
        if (product != null) {
            return product;
        }
        throw new EntityNotFoundException("Cannot find product with ID: " + id);
    }

    @Transactional
    public Product insert(Product product) throws EntityAlreadyExistsException {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
            try {
                em.persist(product);
                return product;
            } catch (EntityExistsException e) {
                throw new EntityAlreadyExistsException("Product already exists");
            }
        }
        throw new EntityAlreadyExistsException("Product already has an ID");
    }

    @Transactional
    public Product addProductWithCategory(Product product, String categoryName) throws EntityAlreadyExistsException, EntityNotFoundException {
        // Validate that the category exists
        ProductCategory category = categoryRepo.findByName(categoryName);
        product.setCategory(category); // Set only the category name
        return insert(product);
    }

    @Transactional
    public Product update(Product product) throws EntityNotFoundException {
        try {
            return em.merge(product);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Cannot find product with ID: " + product.getId());
        }
    }

    @Transactional
    public void delete(UUID id) {
        Product product = em.find(Product.class, id);
        if (product != null) {
            em.remove(product);
        }
    }
}
