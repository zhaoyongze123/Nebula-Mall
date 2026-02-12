package com.nebulamall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.nebulamall.product.entity.ProductAttrValueEntity;

import java.util.Map;
import java.util.List;

/**
 * spu属性值
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 04:52:39
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存产品属性值
     * @param collect 产品属性值集合
     */
    void saveProductAttr(List<ProductAttrValueEntity> collect);
}

