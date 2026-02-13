package com.example.nebulamall.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Product Search Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Search keyword
     */
    private String keyword;

    /**
     * Category ID filter
     */
    private Long categoryId;

    /**
     * Brand ID filter
     */
    private Long brandId;

    /**
     * Min price filter
     */
    private Double minPrice;

    /**
     * Max price filter
     */
    private Double maxPrice;

    /**
     * Page number (0-based)
     */
    private Integer pageNum = 0;

    /**
     * Page size
     */
    private Integer pageSize = 20;

    /**
     * Sort field
     */
    private String sortBy = "id";

    /**
     * Sort order (asc/desc)
     */
    private String sortOrder = "desc";

}
