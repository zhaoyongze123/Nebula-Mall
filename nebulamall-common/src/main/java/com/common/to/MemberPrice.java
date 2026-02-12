package com.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员价格VO/TO
 */
@Data
public class MemberPrice {

    /**
     * 会员等级ID
     */
    private Long id;

    /**
     * 会员等级名称
     */
    private String name;

    /**
     * 会员价格
     */
    private BigDecimal price;
}
