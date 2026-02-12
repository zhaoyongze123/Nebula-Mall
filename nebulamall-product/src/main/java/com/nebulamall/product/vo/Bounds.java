package com.nebulamall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 积分信息VO
 */
@Data
public class Bounds {

    /**
     * 金币
     */
    private BigDecimal buyBounds;

    /**
     * 成长值
     */
    private BigDecimal growBounds;
}
