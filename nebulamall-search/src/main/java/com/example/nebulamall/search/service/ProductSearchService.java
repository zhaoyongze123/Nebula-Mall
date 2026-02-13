package com.example.nebulamall.search.service;

import com.example.nebulamall.search.entity.ProductSearch;
import com.example.nebulamall.search.repository.ProductSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Product Search Service
 * Provides business logic for product search operations
 */
@Slf4j
@Service
public class ProductSearchService {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * Save product to Elasticsearch
     */
    public ProductSearch save(ProductSearch product) {
        log.info("Saving product to Elasticsearch: {}", product.getId());
        return productSearchRepository.save(product);
    }

    /**
     * Save multiple products to Elasticsearch
     */
    public Iterable<ProductSearch> saveAll(Iterable<ProductSearch> products) {
        log.info("Saving multiple products to Elasticsearch");
        return productSearchRepository.saveAll(products);
    }

    /**
     * Search products by keyword (product name)
     */
    public List<ProductSearch> searchByKeyword(String keyword) {
        log.info("Searching products by keyword: {}", keyword);
        return StreamSupport.stream(
                productSearchRepository.findByProductNameContaining(keyword).spliterator(),
                false
        ).collect(Collectors.toList());
    }

    /**
     * Search products by category
     */
    public List<ProductSearch> searchByCategory(Long categoryId) {
        log.info("Searching products by category: {}", categoryId);
        return StreamSupport.stream(
                productSearchRepository.findByCategoryId(categoryId).spliterator(),
                false
        ).collect(Collectors.toList());
    }

    /**
     * Search products by brand
     */
    public List<ProductSearch> searchByBrand(Long brandId) {
        log.info("Searching products by brand: {}", brandId);
        return StreamSupport.stream(
                productSearchRepository.findByBrandId(brandId).spliterator(),
                false
        ).collect(Collectors.toList());
    }

    /**
     * Search products by price range
     */
    public List<ProductSearch> searchByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Searching products by price range: {} - {}", minPrice, maxPrice);
        return StreamSupport.stream(
                productSearchRepository.findByPriceBetween(minPrice, maxPrice).spliterator(),
                false
        ).collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    public ProductSearch getById(Long id) {
        log.info("Getting product by ID: {}", id);
        return productSearchRepository.findById(id).orElse(null);
    }

    /**
     * Delete product by ID
     */
    public void deleteById(Long id) {
        log.info("Deleting product by ID: {}", id);
        productSearchRepository.deleteById(id);
    }

    /**
     * Delete all products
     */
    public void deleteAll() {
        log.info("Deleting all products from Elasticsearch");
        productSearchRepository.deleteAll();
    }

    /**
     * Get total count of products
     */
    public long count() {
        return productSearchRepository.count();
    }

}
