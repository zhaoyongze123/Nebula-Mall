package com.nebulamall.product.vo;

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;
    private String groupName;

    // 所属分类id路径
    private Long[] catelogPath;


    public void setAttrGroupName(String attrGroupName) {
    }
}
