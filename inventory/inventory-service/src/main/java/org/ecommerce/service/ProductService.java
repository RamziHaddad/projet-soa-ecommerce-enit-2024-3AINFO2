package org.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.ecommerce.events.ProductEvent;
import org.ecommerce.model.Product;
import org.ecommerce.repository.ProductRepository;
import com.google.gson.Gson;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PostPersist;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repo;

    @Inject
    @Channel("product-events")
    Emitter<String> productEventEmitter;

    public List<Product> getAllProducts() {
        return repo.listAll();
    }

    @Transactional
    public Product addNewProduct(Product product) {
        repo.addProduct(product);
        sendProductEvent(product, "CREATE");
        return product;
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
            sendProductEvent(updatedProduct, "UPDATE");
            return Optional.of(updatedProduct);
        }
        return Optional.empty();
    }

    @Transactional
    public Product registerReception(UUID productId, int quantity) {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
        repo.updateProduct(product);
        sendProductEvent(product, "UPDATE");
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
        sendProductEvent(product, "UPDATE");
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
        sendProductEvent(product, "UPDATE");
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
        sendProductEvent(product, "UPDATE");
        return product;
    }

    @PostPersist
    private void sendProductEvent(Product product, String eventType) {
        ProductEvent productEvent = new ProductEvent(
                product.getId(),
                product.getTotalQuantity(),
                product.getReservedQuantity(),
                eventType);
        produceProductEvent(productEvent);
    }

    @PostPersist
    public void produceProductEvent(ProductEvent productEvent) {
        String jsonEvent = new Gson().toJson(productEvent);
        productEventEmitter.send(jsonEvent);
    }
}
