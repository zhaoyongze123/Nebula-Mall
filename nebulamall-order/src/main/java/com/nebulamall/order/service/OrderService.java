package com.nebulamall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.nebulamall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 18:35:15
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

