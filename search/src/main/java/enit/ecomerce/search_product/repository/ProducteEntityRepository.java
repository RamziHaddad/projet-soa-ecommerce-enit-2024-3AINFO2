package enit.ecomerce.search_product.repository;

import enit.ecomerce.search_product.product.ProductEntity;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProducteEntityRepository extends CrudRepository<ProductEntity, String> {

    @Query("SELECT p FROM ProductEntity p WHERE p.isIndex = false")
    List<ProductEntity> findUnindexedProducts();
}