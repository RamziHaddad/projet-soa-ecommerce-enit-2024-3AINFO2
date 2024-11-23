package org.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ecommerce.model.Product;
import org.ecommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Inject
    ProductRepository repo;

    public List<Product> getAllProducts() {
        return repo.listAll();
    }

    @Transactional
    public void addNewProduct(Product product) {
        repo.addProduct(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return repo.getProductByID(id);
    }

    @Transactional
    public void removeProductById(UUID id) {
        repo.deleteProductById(id);
    }

    @Transactional
    public Optional<Product> updateProduct(UUID id, Product updatedProduct) {
        Optional<Product> existingProduct = repo.getProductByID(id);
        if (existingProduct.isPresent()) {
            updatedProduct.setId(id);
            repo.updateProduct(updatedProduct);
            return Optional.of(updatedProduct);
        }
        return Optional.empty();
    }

    @Transactional
    public Product registerReception(UUID productId, int quantity) {
        Product product = repo.getProductByID(productId)
                .orElse(null);
        if (product == null) {
            product = new Product(productId, quantity);
            try {
                repo.addProduct(product);
                logger.info("Product successfully added: " + productId);
            } catch (OptimisticLockException e) {
                logger.error("OptimisticLockException occurred while adding product", e);
            }
        } else {
            product.setId(productId);
            product.setTotalQuantity(product.getTotalQuantity() + quantity);
            repo.updateProduct(product);
        }
        return product;
    }

    @Transactional
    public Product reserveProduct(UUID productId, int quantity) {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        if (product.availableQuantity() < quantity) {
            throw new WebApplicationException("Insufficient stock", 400);
        }
        product.setReservedQuantity(product.getReservedQuantity() + quantity);
        repo.updateProduct(product);
        return product;
    }

    @Transactional
    public Product releaseReservation(UUID productId, int quantity) {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        if (product.getReservedQuantity() < 0) {
            product.setReservedQuantity(0);
        }
        repo.updateProduct(product);
        return product;
    }

    @Transactional
    public Product recordOrderShipment(UUID productId, int quantity) {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Produit non trouvé", 404));
        product.setTotalQuantity(product.getTotalQuantity() - quantity);
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        if (product.getReservedQuantity() < 0 || product.getTotalQuantity() < 0) {
            throw new WebApplicationException("Stock insuffisant ou non reservé pour la sortie de commande", 400);
        }
        repo.updateProduct(product);
        return product;
    }
}
