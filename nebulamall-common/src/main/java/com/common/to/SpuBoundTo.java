package com.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * SPU积分信息转换对象，用于远程调用优惠券服务
 */
@Data
public class SpuBoundTo {

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 金币
     */
    private BigDecimal buyBounds;

    /**
     * 成长值
     */
    private BigDecimal growBounds;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public BigDecimal getBuyBounds() {
        return buyBounds;
    }

    public void setBuyBounds(BigDecimal buyBounds) {
        this.buyBounds = buyBounds;
    }

    public BigDecimal getGrowBounds() {
        return growBounds;
    }

    public void setGrowBounds(BigDecimal growBounds) {
        this.growBounds = growBounds;
    }
}
