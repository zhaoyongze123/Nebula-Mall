package com.nebulamall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保存SPU信息的VO对象
 */
@Data
public class SpuSaveVo {

    private String spuName;

    private String spuDescription;

    private Long catalogId;

    private Long brandId;

    private BigDecimal weight;

    private Integer publishStatus;

    /**
     * 商品描述图片集合
     */
    private List<String> decript;

    /**
     * 商品图片集合
     */
    private List<String> images;

    /**
     * 积分信息
     */
    private Bounds bounds;

    /**
     * 基础属性列表
     */
    private List<BaseAttrs> baseAttrs;

    /**
     * SKU列表
     */
    private List<Skus> skus;
}
