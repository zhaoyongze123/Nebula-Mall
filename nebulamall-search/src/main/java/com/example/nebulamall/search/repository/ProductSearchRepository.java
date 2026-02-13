package com.example.nebulamall.search.repository;

import com.example.nebulamall.search.entity.ProductSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Product Search Repository
 * Provides CRUD and search operations for ProductSearch entities
 */
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, Long> {

    /**
     * Search products by name
     */
    Iterable<ProductSearch> findByProductNameContaining(String productName);

    /**
     * Search products by category
     */
    Iterable<ProductSearch> findByCategoryId(Long categoryId);

    /**
     * Search products by brand
     */
    Iterable<ProductSearch> findByBrandId(Long brandId);

    /**
     * Search products by price range
     */
    Iterable<ProductSearch> findByPriceBetween(Double minPrice, Double maxPrice);

}
