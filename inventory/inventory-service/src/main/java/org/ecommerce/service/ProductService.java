package org.ecommerce.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.ecommerce.model.Item;
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
    @Inject
    EventProducerService eventProducerService;

    public List<Product> getAllProducts() {
        return repo.listAll();
    }

    @Transactional
    public Product addNewProduct(Product product) throws JsonProcessingException {
        repo.addProduct(product);
        eventProducerService.sendProductEvent(product, "CREATE");
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
    public Optional<Product> updateProduct(UUID id, Product updatedProduct) throws JsonProcessingException {
        Optional<Product> existingProduct = repo.getProductByID(id);
        if (existingProduct.isPresent()) {
            updatedProduct.setId(id);
            repo.updateProduct(updatedProduct);
            eventProducerService.sendProductEvent(updatedProduct, "UPDATE");
            return Optional.of(updatedProduct);
        }
        return Optional.empty();
    }

    @Transactional
    public Product registerReception(UUID productId, int quantity) throws JsonProcessingException {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        if(product.getTotalQuantity()==0 && quantity>0) eventProducerService.sendMinimalEvent(productId,"IN_STOCK");
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
        repo.updateProduct(product);
        eventProducerService.sendProductEvent(product, "UPDATE");
        return product;
    }

    @Transactional
    public Product reserveProduct(UUID productId, int quantity) throws JsonProcessingException {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        if (product.availableQuantity() < quantity) {
            throw new WebApplicationException("Insufficient stock", 400);
        }
        product.setReservedQuantity(product.getReservedQuantity() + quantity);
        repo.updateProduct(product);
        eventProducerService.sendProductEvent(product, "UPDATE");
        return product;
    }

    @Transactional
    public Product releaseReservation(UUID productId, int quantity) throws JsonProcessingException {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        if (product.getReservedQuantity() < 0) {
            product.setReservedQuantity(0);
        }
        repo.updateProduct(product);
        eventProducerService.sendProductEvent(product, "UPDATE");
        return product;
    }

    @Transactional
    public Product recordOrderShipment(UUID productId, int quantity) throws JsonProcessingException {
        Product product = repo.getProductByID(productId)
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));
        product.setTotalQuantity(product.getTotalQuantity() - quantity);
        product.setReservedQuantity(product.getReservedQuantity() - quantity);
        if(product.getTotalQuantity()==0){
            eventProducerService.sendMinimalEvent(productId,"OUT_Of_STOCK");
        }
        if (product.getReservedQuantity() < 0 || product.getTotalQuantity() < 0) {
            throw new WebApplicationException("Stock insuffisant ou non reservé pour la sortie de commande", 400);
        }
        repo.updateProduct(product);
        eventProducerService.sendProductEvent(product, "UPDATE");
        return product;
    }

    @Transactional
    public Boolean checkAvailibilityProduct(Item item){
        Product product = repo.getProductByID(item.getId())
                .orElseThrow(() -> new WebApplicationException("Product not found", 404));

        return product.availableQuantity() >= item.getQuantity();
    }


}
