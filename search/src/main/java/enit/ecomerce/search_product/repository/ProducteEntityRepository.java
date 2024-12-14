package enit.ecomerce.search_product.repository;

import enit.ecomerce.search_product.product.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducteEntityRepository extends JpaRepository<ProductEntity, String> {

    @Query(value = "SELECT * FROM product_entity WHERE is_indexed = false LIMIT :batchSize", nativeQuery = true)
    List<ProductEntity> findUnindexedProducts(@Param("batchSize") int batchSize);
}
