package com.nebulamall.product.vo;

import lombok.Data;

/**
 * 属性&属性分组关联VO
 */
@Data
public class AttrGroupRelationVo {
    
    /**
     * 关联关系id
     */
    private Long id;
    
    /**
     * 属性id
     */
    private Long attrId;
    
    /**
     * 属性分组id
     */
    private Long attrGroupId;
    
    /**
     * 属性组内排序
     */
    private Integer attrSort;
    
    /**
     * 属性名
     */
    private String attrName;
    
    /**
     * 属性图标
     */
    private String icon;
    
    /**
     * 可选值列表
     */
    private String valueSelect;
}
