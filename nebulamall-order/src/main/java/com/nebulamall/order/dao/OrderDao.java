package com.nebulamall.order.dao;

import com.nebulamall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 18:35:15
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
