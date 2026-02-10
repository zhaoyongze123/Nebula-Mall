package com.nebulamall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.common.constant.ProductConstant;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.nebulamall.product.dao.AttrAttrgroupRelationDao;
import com.nebulamall.product.dao.AttrGroupDao;
import com.nebulamall.product.dao.CategoryDao;
import com.nebulamall.product.entity.AttrAttrgroupRelationEntity;
import com.nebulamall.product.entity.AttrGroupEntity;
import com.nebulamall.product.entity.CategoryEntity;
import com.nebulamall.product.service.AttrAttrgroupRelationService;
import com.nebulamall.product.service.CategoryService;
import com.nebulamall.product.vo.AttrRespVo;
import com.nebulamall.product.vo.AttrVo;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.AttrDao;
import com.nebulamall.product.entity.AttrEntity;
import com.nebulamall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId, String attrType) {
        // 获取搜索关键字
        Object keyObj = params.get("key");
        String key = keyObj != null ? keyObj.toString().trim() : "";

        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>();

        // 当 catelogId 大于 0 时，需要根据具体的分类进行过滤
        if(catelogId > 0){
            wrapper.eq("catelog_id", catelogId);
        }

        // 根据 attrType 过滤属性类型（base=1, sale=0）
        int attrTypeValue = "base".equals(attrType) ? 1 : 0;
        wrapper.eq("attr_type", attrTypeValue);

        // 如果 key 不为空，则添加过滤条件，支持根据 attr_id 精确匹配或者 attr_name 模糊匹配
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        // 将 AttrEntity 转换为 AttrRespVo，并填充分类名称和分组信息
        IPage<AttrRespVo> respPage = page.convert(attr -> {
            AttrRespVo respVo = new AttrRespVo();
            BeanUtils.copyProperties(attr, respVo);

            // 获取分类名称
            CategoryEntity categoryEntity = categoryDao.selectById(attr.getCatelogId());
            if (categoryEntity != null) {
                respVo.setCatelogName(categoryEntity.getName());
            }

            // 获取分组信息
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId())
            );
            if (relationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            return respVo;
        });

        return new PageUtils(respPage);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        // 将 AttrVo 中的属性复制到 AttrEntity 中
        BeanUtils.copyProperties(attr, attrEntity);

        // 保存到数据库
        this.save(attrEntity);

        // 如果提供了属性分组ID，则保存关联关系
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) { // 只有基本属性才有分组关系, 销售属性没有分组关系 common.constant.ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()
            if (attr.getAttrGroupId() != null && attr.getAttrGroupId() > 0) {
                AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                relationEntity.setAttrId(attrEntity.getAttrId());
                relationEntity.setAttrGroupId(attr.getAttrGroupId());// 设置分组ID
                relationDao.insert(relationEntity);
            }
        }
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        // 将 AttrVo 中的属性复制到 AttrEntity 中
        BeanUtils.copyProperties(attr, attrEntity);

        // 更新属性到数据库
        this.updateById(attrEntity);

        // 只有基本属性才有分组关系, 销售属性没有分组关系
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 先删除原有的属性分组关联关系
            relationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));

            // 如果提供了属性分组ID，则保存新的关联关系
            if (attr.getAttrGroupId() != null && attr.getAttrGroupId() > 0) {
                AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
                relationEntity.setAttrId(attr.getAttrId());
                relationEntity.setAttrGroupId(attr.getAttrGroupId());
                relationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {

        AttrEntity attrEntity = this.getById(attrId);
        if (attrEntity == null) {
            return null;
        }
        
        AttrRespVo respVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, respVo);

        // 查询属性分组信息
        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(
            new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)
        );
        if (relationEntity != null) {
            respVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity =
                attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                respVo.setAttrGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        
        // 查询分类信息（应该总是查询，不依赖于分组关联）
        Long catelogId = attrEntity.getCatelogId();
        if (catelogId != null) {
            Long[] catelogPath = categoryService.findCatelogPath(catelogId);
            respVo.setCatelogPath(catelogPath);
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            if (categoryEntity != null) {
                respVo.setCatelogName(categoryEntity.getName());
            }
        }

        return respVo;
    }
    
    @Override
    public List<com.nebulamall.product.vo.AttrGroupRelationAttrVo> getRelationAttr(Long attrGroupId) {
        // 查询该属性分组下的所有属性关联关系
        List<AttrAttrgroupRelationEntity> relationList = relationDao.selectList(
            new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId)
        );
        
        // 将关联关系转换为属性响应VO列表，过滤掉null结果
        return relationList.stream().map(relation -> {
            com.nebulamall.product.vo.AttrGroupRelationAttrVo vo = new com.nebulamall.product.vo.AttrGroupRelationAttrVo();
            AttrRespVo attrInfo = this.getAttrInfo(relation.getAttrId());
            if (attrInfo != null) {
                BeanUtils.copyProperties(attrInfo, vo);
                vo.setRelationId(relation.getId());
            }
            return vo;
        }).filter(attr -> attr != null && attr.getAttrId() != null).collect(Collectors.toList());
    }
    
    @Transactional
    @Override
    public void deleteRelation(Long attrGroupId, Long[] attrIds) {
        // 删除指定属性分组中的属性关联关系
        for (Long attrId : attrIds) {
            relationDao.delete(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", attrGroupId)
                    .eq("attr_id", attrId)
            );
        }
    }
    
    @Transactional
    @Override
    public void addRelation(List<com.nebulamall.product.vo.AttrGroupRelationDeleteVo> relationList) {
        // 新增属性分组关联关系
        for (com.nebulamall.product.vo.AttrGroupRelationDeleteVo vo : relationList) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(vo.getAttrId());
            relationEntity.setAttrGroupId(vo.getAttrGroupId());
            relationDao.insert(relationEntity);
        }
    }

    /*
        * 获取属性分组没有关联的属性列表
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {
        // 1. 当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();// 获取当前分组所属的分类ID

        // 2. 当前分组只能关联别的分组没有引用的属性，也不能关联自己已经关联的属性
        //2.1）、当前分类下的所有分组（包括当前分组）
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

         List<Long> collect = group.stream().map(item ->
                item.getAttrGroupId()).collect(Collectors.toList());

        //2.2）、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));

        List<Long> attrIds = groupId.stream().map(item ->
                item.getAttrId()).collect(Collectors.toList());

        //2.3）、从当前分类的所有属性中移除这些属性；
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds);
        }

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


}