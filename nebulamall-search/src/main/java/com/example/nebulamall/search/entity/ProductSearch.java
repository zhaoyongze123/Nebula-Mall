package com.example.nebulamall.search.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Product Search Entity
 * Represents a product document in Elasticsearch index
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "nebula_product")
public class ProductSearch {

    @Id
    @JsonProperty("id")
    private Long id;

    @Field(type = FieldType.Keyword)
    @JsonProperty("sku_id")
    private Long skuId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @JsonProperty("product_name")
    private String productName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @JsonProperty("product_description")
    private String productDescription;

    @Field(type = FieldType.Keyword)
    @JsonProperty("category_id")
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    @JsonProperty("category_name")
    private String categoryName;

    @Field(type = FieldType.Keyword)
    @JsonProperty("brand_id")
    private Long brandId;

    @Field(type = FieldType.Keyword)
    @JsonProperty("brand_name")
    private String brandName;

    @Field(type = FieldType.Double)
    @JsonProperty("price")
    private Double price;

    @Field(type = FieldType.Integer)
    @JsonProperty("sales")
    private Integer sales;

    @Field(type = FieldType.Keyword)
    @JsonProperty("product_image")
    private String productImage;

    @Field(type = FieldType.Date)
    @JsonProperty("create_time")
    private Long createTime;

    @Field(type = FieldType.Date)
    @JsonProperty("update_time")
    private Long updateTime;

}
