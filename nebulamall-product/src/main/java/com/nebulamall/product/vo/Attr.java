package com.nebulamall.product.vo;

import lombok.Data;

/**
 * 销售属性VO
 */
@Data
public class Attr {

    /**
     * 销售属性ID
     */
    private Long attrId;

    /**
     * 销售属性名称
     */
    private String attrName;

    /**
     * 销售属性值
     */
    private String attrValue;
}
