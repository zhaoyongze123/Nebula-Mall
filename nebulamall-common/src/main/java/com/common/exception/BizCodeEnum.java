package com.common.exception;

/**
 * 错误码枚举
 * 
 * 错误码定义规则为 5位数字
 * 前两位表示业务场景，最后三位表示错误码
 * 例如：100001 表示 10（通用）+ 001（系统未知异常）
 * 
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-09
 */
public enum BizCodeEnum {
    
    // ==================== 10:通用 ====================
    UNKNOWN_EXCEPTION(10001, "系统未知异常"),
    VALID_EXCEPTION(10002, "参数格式校验失败"),
    TO_MANY_REQUEST(10002, "请求过于频繁，请稍后再试"),
    SMS_CODE_EXCEPTION(10003, "验证码获取频率过高，请稍后再试"),
    
    // ==================== 11:商品 ====================
    PRODUCT_UP_EXCEPTION(11001, "商品上架异常"),
    PRODUCT_CATEGORY_NOT_FOUND(11002, "商品分类不存在"),
    BRAND_NOT_FOUND(11003, "品牌不存在"),
    PRODUCT_NOT_FOUND(11004, "商品不存在"),
    PRODUCT_PARAM_INVALID(11005, "商品参数无效"),
    
    // ==================== 12:订单 ====================
    ORDER_CREATE_EXCEPTION(12001, "订单创建失败"),
    ORDER_NOT_FOUND(12002, "订单不存在"),
    ORDER_STATUS_INVALID(12003, "订单状态非法"),
    ORDER_PAYMENT_EXCEPTION(12004, "订单支付异常"),
    
    // ==================== 13:购物车 ====================
    CART_ITEM_NOT_FOUND(13001, "购物车项目不存在"),
    CART_QUANTITY_INVALID(13002, "购物车数量无效"),
    
    // ==================== 14:物流 ====================
    LOGISTICS_TRACKING_FAILED(14001, "物流跟踪失败"),
    ADDRESS_NOT_FOUND(14002, "收货地址不存在"),
    
    // ==================== 15:用户 ====================
    ACCOUNT_NOT_FOUND(15001, "账户不存在"),
    ACCOUNT_LOCKED(15002, "账户已锁定"),
    PASSWORD_ERROR(15003, "密码错误"),
    
    // ==================== 16:库存 ====================
    NO_STOCK_EXCEPTION(16001, "库存不足"),
    STOCK_LOCK_FAILED(16002, "锁定库存失败");
    
    private final int code;
    private final String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
