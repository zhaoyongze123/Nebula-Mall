package com.example.nebulamall.search.controller;

import com.example.nebulamall.search.entity.ProductSearch;
import com.example.nebulamall.search.service.ProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Search Controller
 * Provides REST endpoints for product search operations
 */
@Slf4j
@RestController
@RequestMapping("/search/product")
public class ProductSearchController {

    @Autowired
    private ProductSearchService productSearchService;

    /**
     * Search products by keyword
     * GET /search/product/keyword?q=中文分词
     */
    @GetMapping("/keyword")
    public List<ProductSearch> searchByKeyword(@RequestParam("q") String keyword) {
        log.info("REST: Search by keyword: {}", keyword);
        return productSearchService.searchByKeyword(keyword);
    }

    /**
     * Search products by category
     * GET /search/product/category/1
     */
    @GetMapping("/category/{categoryId}")
    public List<ProductSearch> searchByCategory(@PathVariable Long categoryId) {
        log.info("REST: Search by category: {}", categoryId);
        return productSearchService.searchByCategory(categoryId);
    }

    /**
     * Search products by brand
     * GET /search/product/brand/1
     */
    @GetMapping("/brand/{brandId}")
    public List<ProductSearch> searchByBrand(@PathVariable Long brandId) {
        log.info("REST: Search by brand: {}", brandId);
        return productSearchService.searchByBrand(brandId);
    }

    /**
     * Search products by price range
     * GET /search/product/price?min=10&max=100
     */
    @GetMapping("/price")
    public List<ProductSearch> searchByPrice(
            @RequestParam("min") Double minPrice,
            @RequestParam("max") Double maxPrice) {
        log.info("REST: Search by price: {} - {}", minPrice, maxPrice);
        return productSearchService.searchByPriceRange(minPrice, maxPrice);
    }

    /**
     * Get product by ID
     * GET /search/product/1
     */
    @GetMapping("/{id}")
    public ProductSearch getProduct(@PathVariable Long id) {
        log.info("REST: Get product by ID: {}", id);
        return productSearchService.getById(id);
    }

    /**
     * Save product to search index
     * POST /search/product
     */
    @PostMapping
    public ProductSearch saveProduct(@RequestBody ProductSearch product) {
        log.info("REST: Save product: {}", product.getId());
        return productSearchService.save(product);
    }

    /**
     * Delete product from search index
     * DELETE /search/product/1
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        log.info("REST: Delete product: {}", id);
        productSearchService.deleteById(id);
    }

    /**
     * Get total count of products in index
     * GET /search/product/count
     */
    @GetMapping("/count")
    public long count() {
        log.info("REST: Get total count");
        return productSearchService.count();
    }

}
