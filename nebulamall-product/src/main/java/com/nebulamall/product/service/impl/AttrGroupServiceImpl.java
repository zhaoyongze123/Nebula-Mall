package com.nebulamall.product.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.AttrGroupDao;
import com.nebulamall.product.entity.AttrGroupEntity;
import com.nebulamall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

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

}