package enit.ecomerce.search_product.service;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
        @Retryable(
        value = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 1.5)
    )
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findProductById(String id) {
        return productRepository.findById(id);
    }

    public Iterable<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(String id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    public void deleteProductById(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String inputQuery) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("name", "description")
                                .query(inputQuery)))
                .build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
        return searchHits.stream().map(s -> s.getContent()).toList();
    }

    public List<Product> autocompleteSuggestions(String inputQuery) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.wildcard(w -> w
                                        .field("name")
                                        .value("*" + inputQuery.toLowerCase() + "*")))))

                .build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
        return searchHits.stream().map(s -> s.getContent()).toList();
    }

    public List<Product> fuzzySearch(String inputQuery) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s.fuzzy(f -> f
                                        .field("name")
                                        .value(inputQuery)
                                        .fuzziness("AUTO")))
                                .should(s -> s.fuzzy(f -> f
                                        .field("description")
                                        .value(inputQuery)
                                        .fuzziness("AUTO")))))

                .build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
        return searchHits.stream().map(s -> s.getContent()).toList();
    }

}
