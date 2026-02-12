package com.nebulamall.product.vo;

import lombok.Data;

/**
 * 基础属性VO
 */
@Data
public class BaseAttrs {

    /**
     * 属性ID
     */
    private Long attrId;

    /**
     * 属性值，多个值用;隔开
     */
    private String attrValues;

    /**
     * 是否快速展示: 0不展示 1展示
     */
    private int showDesc;
}
