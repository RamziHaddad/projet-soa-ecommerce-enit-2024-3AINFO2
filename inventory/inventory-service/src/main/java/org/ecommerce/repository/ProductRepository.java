package org.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ecommerce.model.Product;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductRepository {
    @Inject
    EntityManager em;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        em.createQuery("DELETE FROM Product p").executeUpdate();
   }

    @Transactional
    public List<Product> listAll() {
        return em.createQuery("from Product t ", Product.class).getResultList();
    }

    @Transactional
    public Optional<Product> getProductByID(UUID id) {
        Product product = em.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Transactional
    public Product addProduct(Product product) {
        em.merge(product);
        return product;
    }

    @Transactional
    public Product updateProduct(Product product) {
        return em.merge(product);
    }

    @Transactional
    public void deleteProductById(UUID id) {
        Product product = em.find(Product.class, id);
        if (product != null) {
            em.remove(product);
        }
    }
}
