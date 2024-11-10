package enit.ecomerce.search_product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.elasticsearch.core.query.*;
import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.repository.ProductRepository;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class ProductController {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductRepository repository;

    @PostMapping
    public Product create(@RequestBody Product Product) {
        return repository.save(Product);
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable String id) {
        return repository.findById(id);
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product Product) {
        Product.setId(id);
        return repository.save(Product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Product> searchEmployees(@RequestParam String inputQuery) {

        Query query = NativeQuery.builder()
                 
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("name","description","category")
                                .query(inputQuery)))
        
                .build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class); 
        return searchHits.stream().map(s->s.getContent()).toList();
    }
}
