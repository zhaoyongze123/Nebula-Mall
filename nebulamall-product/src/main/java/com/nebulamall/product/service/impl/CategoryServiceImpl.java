package com.nebulamall.product.service.impl;

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

import com.nebulamall.product.dao.CategoryDao;
import com.nebulamall.product.entity.CategoryEntity;
import com.nebulamall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    // 查出所有分类以及子分类，以树形结构组装起来
    @Override
    public List<CategoryEntity> listWithTree() {
        // 1. 查出所有分类
        List<CategoryEntity> entities = this.baseMapper.selectList(null);

        // 2. 组装成父子的树形结构

        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity -> {
            //2.1 找到所有的一级分类
            return categoryEntity.getParentCid() == 0;
        }).map(menu -> {
            //2.2 递归查找子分类
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1,menu2)->{
            //2.3 按照sort字段进行排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }



    private List<CategoryEntity> getChildrens(CategoryEntity menu, List<CategoryEntity> entities) {
        List<CategoryEntity> children = entities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(menu.getCatId());
        }).map(categoryEntity -> {
            // 递归查找子分类
            categoryEntity.setChildren(getChildrens(categoryEntity, entities));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // 按照sort字段进行排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }



    @Override
    public void removemenuByIds(List<Long> list) {
        // TODO 1. 检查当前删除的菜单，是否被别的地方引用

        // 逻辑删除
        this.baseMapper.deleteBatchIds(list);

    }

}