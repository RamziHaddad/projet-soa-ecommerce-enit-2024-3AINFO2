package org.enit.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.enit.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductRepository {
    @Inject
    EntityManager em;

    @Transactional
    public List<Product> listAll(){
        return em.createQuery("from Product t ",Product.class).getResultList();
    }

    @Transactional
    public Optional<Product> getProductByID(UUID id){
        Product product=em.find(Product.class,id);
        return Optional.ofNullable(product);
    }
    @Transactional
    public void addProduct(Product product){
        em.persist(product);

    }

    @Transactional
    public Product updateProduct(Product product){
       return em.merge(product);
    }

    @Transactional
    public void deleteProductById(UUID id){
        Product product=em.find(Product.class,id);
        if(product!=null){
            em.remove(product);
        }
    }


}
