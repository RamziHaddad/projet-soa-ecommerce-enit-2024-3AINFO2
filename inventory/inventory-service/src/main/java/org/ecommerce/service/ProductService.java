package org.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ecommerce.model.Product;
import org.ecommerce.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class ProductService {

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

    public Product registerReception(String productId, int quantity) {
        UUID productUUID = UUID.fromString(productId);
        Product product = repo.getProductByID(productUUID)
                .orElse(repo.addProduct(new Product(productUUID, quantity)));
        product.setId(productUUID);
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
        repo.updateProduct(product);
        return product;
    }

    public Product reserveProduct(String productId, int quantity) {
        UUID productUUID = UUID.fromString(productId);
        Product product = repo.getProductByID(productUUID)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        if (product.availableQuantity() < quantity) {
            throw new WebApplicationException("Insufficient stock", 400);
        }
        product.setReservedQuantity(product.getReservedQuantity() + quantity);
        repo.updateProduct(product);
        return product;
    }

    public Product releaseReservation(String productId, int quantity) {
        UUID productUUID = UUID.fromString(productId);
        Product product = repo.getProductByID(productUUID)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        if (product.getReservedQuantity() < 0) {
            product.setReservedQuantity(0);
        }
        repo.updateProduct(product);
        return product;
    }
}
