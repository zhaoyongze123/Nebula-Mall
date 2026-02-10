package com.nebulamall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.nebulamall.product.entity.AttrEntity;
import com.nebulamall.product.vo.AttrRespVo;
import com.nebulamall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 04:52:39
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType);

    void saveAttr(AttrVo attr);

    void updateAttr(AttrVo attr);

    AttrRespVo getAttrInfo(Long attrId);

    /**
     * 获取属性分组关联的所有属性列表
     */
    List<com.nebulamall.product.vo.AttrGroupRelationAttrVo> getRelationAttr(Long attrGroupId);

    /**
     * 删除属性分组中指定的属性关联
     */
    void deleteRelation(Long attrGroupId, Long[] attrIds);
    
    /**
     * 新增属性分组关联
     */
    void addRelation(List<com.nebulamall.product.vo.AttrGroupRelationDeleteVo> relationList);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);
}