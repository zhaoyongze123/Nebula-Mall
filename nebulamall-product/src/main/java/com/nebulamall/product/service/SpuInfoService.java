package com.nebulamall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.nebulamall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 04:52:39
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SPU信息
     * @param vo SPU保存VO
     */
    void saveSpuInfo(com.nebulamall.product.vo.SpuSaveVo vo);

    /**
     * 保存SPU基本信息
     * @param infoEntity SPU信息实体
     */
    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    /**
     * 根据条件分页查询SPU
     * @param params 查询参数
     * @return 分页结果
     */
    PageUtils queryPageByCondition(Map<String, Object> params);
}

