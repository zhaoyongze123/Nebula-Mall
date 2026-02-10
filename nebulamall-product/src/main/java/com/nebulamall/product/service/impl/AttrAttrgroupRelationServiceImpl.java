package com.nebulamall.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.AttrAttrgroupRelationDao;
import com.nebulamall.product.dao.AttrDao;
import com.nebulamall.product.entity.AttrAttrgroupRelationEntity;
import com.nebulamall.product.entity.AttrEntity;
import com.nebulamall.product.service.AttrAttrgroupRelationService;
import com.nebulamall.product.vo.AttrGroupRelationVo;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String attrGroupId = (String) params.get("attrGroupId");
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        if (attrGroupId != null && !attrGroupId.isEmpty()) {
            queryWrapper.eq("attr_group_id", attrGroupId);
        }
        
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                queryWrapper
        );

        // 转换为VO，并关联查询属性信息
        List<AttrGroupRelationVo> voList = page.getRecords().stream().map(relation -> {
            AttrGroupRelationVo vo = new AttrGroupRelationVo();
            vo.setId(relation.getId());
            vo.setAttrId(relation.getAttrId());
            vo.setAttrGroupId(relation.getAttrGroupId());
            vo.setAttrSort(relation.getAttrSort());
            
            // 查询属性详情
            AttrEntity attr = attrDao.selectById(relation.getAttrId());
            if (attr != null) {
                vo.setAttrName(attr.getAttrName());
                vo.setIcon(attr.getIcon());
                vo.setValueSelect(attr.getValueSelect());
            }
            return vo;
        }).collect(Collectors.toList());

        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(voList);
        return pageUtils;
    }

}