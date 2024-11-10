package enit.ecomerce.search_product.repository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import enit.ecomerce.search_product.product.Product;
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
}
