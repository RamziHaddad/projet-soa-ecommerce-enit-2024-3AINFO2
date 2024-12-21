package com.enit.pricing.repositories;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Repository
public class ProductRepository  {

  @Autowired
  private EntityManager entityManager; 

    @Transactional
    public void addProduct(Product product) {
         if (product.getProductId() != null && entityManager.find(Product.class, product.getProductId()) != null) {
            throw new IllegalStateException("Product already exists with ID: " + product.getProductId());
        } 
        entityManager.persist(product);
    }
 
    @Transactional
    public void updateProduct(Product product) {
        if (entityManager.find(Product.class, product.getProductId()) == null) {
            throw new EntityNotFoundException("Product not found with ID: " + product.getProductId());
        }
        entityManager.merge(product);
    }
    
    @Transactional
    public void setBasePrice(UUID productId, BigDecimal price) {
        String queryCurr = "select p.currentPrice from Product p where p.productId = :productId";
        String queryUpdateBase = "update Product p set p.basePrice = :price where p.productId = :productId";
        String queryUpdateBoth = "update Product p set p.basePrice = :price, p.currentPrice = :price where p.productId = :productId";

        try {
          //fetch current price to check if its null or not
            BigDecimal currPrice = entityManager.createQuery(queryCurr, BigDecimal.class)
                    .setParameter("productId", productId)
                    .getSingleResult();

            if (currPrice == null) { //current price is by default base price
                int updated = entityManager.createQuery(queryUpdateBoth)
                    .setParameter("price", price)
                    .setParameter("productId", productId)
                    .executeUpdate();
                if (updated == 0) {
                    throw new EntityNotFoundException("Product not found with ID: " + productId);
                }
            } else { //set base price only
                int updated = entityManager.createQuery(queryUpdateBase)
                    .setParameter("price", price)
                    .setParameter("productId", productId)
                    .executeUpdate();
                if (updated == 0) {
                    throw new EntityNotFoundException("Product not found with ID: " + productId);
                }
            }
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }


  @Transactional
  public void setCurrentPrice(UUID prodId){
    setCurrentPrice(prodId, null);
  }

    @Transactional
    public void setCurrentPrice(UUID prodId, BigDecimal price) {
        String query;
        if (price == null) {
            query = "update Product p set p.currentPrice=p.basePrice where p.productId= :prodId";
        } else {
            query = "update Product p set p.currentPrice= :price where p.productId= :prodId";
        }
        int updated = entityManager.createQuery(query)
            .setParameter("prodId", prodId)
            .setParameter(price != null ? "price" : "prodId", price != null ? price : prodId)
            .executeUpdate();
        if (updated == 0) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
    }

    @Transactional
    public void deleteProduct(UUID prodId) {
        Product product = findById(prodId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
        entityManager.remove(product);
    }

    @Transactional
    public Product findById(UUID prodId) {
        Product product = entityManager.find(Product.class, prodId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
        return product;
    }

    @Transactional
    public BigDecimal getBasePrice(UUID prodId) {
        try {
            return entityManager.createQuery("select p.basePrice from Product p where p.productId= :prodId", BigDecimal.class)
                .setParameter("prodId", prodId)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
    }

    @Transactional
    public BigDecimal getCurrentPrice(UUID prodId) {
        try {
            return entityManager.createQuery("select p.currentPrice from Product p where p.productId= :prodId", BigDecimal.class)
                .setParameter("prodId", prodId)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
    }
   
    @Transactional
    public void addPromotiontoProduct(UUID prodId, ProductPromotion promotion) {
        int updated = entityManager.createQuery("update Product p set p.promotion= :promotion where p.productId= :prodId")
            .setParameter("prodId", prodId)
            .setParameter("promotion", promotion)
            .executeUpdate();
        if (updated == 0) {
            throw new EntityNotFoundException("Product not found with ID: " + prodId);
        }
    }

    @Transactional
    public void removePromotionFromProduct(UUID productId) {
        Product product = entityManager.find(Product.class, productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + productId + " not found");
        }
        product.setPromotion(null);
        entityManager.merge(product);
    }
}
