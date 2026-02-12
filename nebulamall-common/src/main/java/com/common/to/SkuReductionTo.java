package com.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU优惠信息转换对象，用于远程调用优惠券服务
 */
@Data
public class SkuReductionTo {

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 满足件数的需要优惠的积分数
     */
    private Integer fullCount;

    /**
     * 优惠折扣率
     */
    private BigDecimal discount;

    /**
     * 满足件数条件的优惠金额（如果为0表示不参与满件条件）
     */
    private BigDecimal countPrice;

    /**
     * 满足金额的需要优惠的金额
     */
    private BigDecimal fullPrice;

    /**
     * 优惠金额
     */
    private BigDecimal reducePrice;

    /**
     * 会员价格列表
     */
    private List<MemberPrice> memberPrice;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getFullCount() {
        return fullCount;
    }

    public void setFullCount(Integer fullCount) {
        this.fullCount = fullCount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getCountPrice() {
        return countPrice;
    }

    public void setCountPrice(BigDecimal countPrice) {
        this.countPrice = countPrice;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }

    public List<MemberPrice> getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(List<MemberPrice> memberPrice) {
        this.memberPrice = memberPrice;
    }
}
