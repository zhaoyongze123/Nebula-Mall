package com.nebulamall.product.service.impl;

import com.nebulamall.product.dao.BrandDao;
import com.nebulamall.product.dao.CategoryDao;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.CategoryBrandRelationDao;
import com.nebulamall.product.entity.CategoryBrandRelationEntity;
import com.nebulamall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {


    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        val brandId = categoryBrandRelation.getBrandId();
        val catelogId = categoryBrandRelation.getCatelogId();

        //1.查询品牌的名字
        val brandEntity = this.brandDao.selectById(brandId);
        if (brandEntity != null) {
            categoryBrandRelation.setBrandName(brandEntity.getName());
        }

        //2.查询分类的名字
        val catelogEntity = this.categoryDao.selectById(catelogId);
        if (catelogEntity != null) {
            categoryBrandRelation.setCatelogName(catelogEntity.getName());
        }


        //4.保存到数据库
        this.save(categoryBrandRelation);


    }

}