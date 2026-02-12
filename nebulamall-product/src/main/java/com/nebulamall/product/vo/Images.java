package com.nebulamall.product.vo;

import lombok.Data;

/**
 * 图片信息VO
 */
@Data
public class Images {

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 是否为默认图片: 0不是 1是
     */
    private int defaultImg;
}
