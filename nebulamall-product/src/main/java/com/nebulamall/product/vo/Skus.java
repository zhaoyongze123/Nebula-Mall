package com.nebulamall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU信息VO
 */
@Data
public class Skus {

    /**
     * SKU属性列表
     */
    private List<Attr> attr;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * SKU价格
     */
    private BigDecimal price;

    /**
     * SKU标题
     */
    private String skuTitle;

    /**
     * SKU副标题
     */
    private String skuSubtitle;

    /**
     * 图片集合
     */
    private List<Images> images;

    /**
     * 销售属性值信息：颜色、内存等
     */
    private List<String> descar;

    /**
     * 满减件数
     */
    private int fullCount;

    /**
     * 折扣比例
     */
    private BigDecimal discount;

    /**
     * 满件条件状态: 0为不满足条件，1为满足条件
     */
    private int countStatus;

    /**
     * 满减金额
     */
    private BigDecimal fullPrice;

    /**
     * 优惠价格
     */
    private BigDecimal reducePrice;

    /**
     * 满金额条件状态: 0为不满足条件，1为满足条件
     */
    private int priceStatus;

    /**
     * 会员价格列表
     */
    private List<MemberPrice> memberPrice;
}
