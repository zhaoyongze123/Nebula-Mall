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
        //当传过来的分类 ID 为 0 时，意味着用户没有点击左侧的树形菜单，或者想要查看所有分类下的属性分组。
        if(catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>()
            );

            return new PageUtils(page);
        }else {
            //当 catelogId 大于 0 时，需要根据具体的分类进行过滤。
            String key = (String) params.get("key"); // 获取检索关键字
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);

            //如果 key 不为空，则添加额外的过滤条件，支持根据 attr_group_id 精确匹配或者 attr_group_name 模糊匹配。
            //obj.eq("attr_group_id", key)：判断 ID 是否等于关键字。
            //.or().like("attr_group_name", key)：或者名称中包含关键字。
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

}