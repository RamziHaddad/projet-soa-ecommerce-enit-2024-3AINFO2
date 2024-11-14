package enit.ecomerce.search_product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService ProductService;

    @PostMapping
    public Product create(@RequestBody Product Product) {
        return ProductService.createProduct(Product);
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable String id) {
        return ProductService.findProductById(id);
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return ProductService.findAllProducts();
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product Product) {

        return ProductService.updateProduct(id, Product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        ProductService.deleteProductById(id);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String inputQuery) {

        return ProductService.searchProducts(inputQuery);

    }

    @GetMapping("/autocompletesubstring")
    public List<Product> autocompleteSuggestions(@RequestParam String inputQuery) {
        return ProductService.autocompleteSuggestions(inputQuery);
    }

    @GetMapping("/fuzzySearch")
    public List<Product> fuzzySearch(@RequestParam String inputQuery) {
        return ProductService.fuzzySearch(inputQuery);
    }

}
