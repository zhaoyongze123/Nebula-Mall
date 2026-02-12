package com.nebulamall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.nebulamall.product.service.AttrService;
import com.nebulamall.product.vo.AttrGroupRelationAttrVo;
import com.nebulamall.product.vo.AttrGroupWithAttrsVo;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.AttrGroupDao;
import com.nebulamall.product.entity.AttrEntity;
import com.nebulamall.product.entity.AttrGroupEntity;
import com.nebulamall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        // 获取检索关键字
        Object keyObj = params.get("key");
        String key = keyObj != null ? keyObj.toString().trim() : "";

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();

        // 当 catelogId 大于 0 时，需要根据具体的分类进行过滤
        if(catelogId > 0){
            wrapper.eq("catelog_id", catelogId);
        }

        // 如果 key 不为空，则添加过滤条件，支持根据 attr_group_id 精确匹配或者 attr_group_name 模糊匹配
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupWithAttrsVo> collect = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId))
                .stream()
                .map(attrGroupEntity -> {
                    AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
                    // 1.复制属性分组的基本信息
                    BeanUtils.copyProperties(attrGroupEntity, vo);
                    // 2.查询当前属性分组关联的所有属性列表
                    List<AttrGroupRelationAttrVo> relationAttrs = attrService.getRelationAttr(vo.getAttrGroupId());
                    // 3.将属性列表转换为List<AttrEntity>并设置到vo对象
                    vo.setAttrs(relationAttrs.stream()
                            .map(relationAttr -> {
                                AttrEntity attr = new AttrEntity();
                                BeanUtils.copyProperties(relationAttr, attr);
                                return attr;
                            })
                            .collect(Collectors.toList()));
                    return vo;
                }).collect(Collectors.toList());
        return collect;
    }

}