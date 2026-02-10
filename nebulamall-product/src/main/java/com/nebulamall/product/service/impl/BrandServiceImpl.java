package com.nebulamall.product.service.impl;

import lombok.val;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.BrandDao;
import com.nebulamall.product.entity.BrandEntity;
import com.nebulamall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //模糊查询
        Object keyObj = params.get("key"); // 获取检索关键字
        String key = keyObj != null ? keyObj.toString().trim() : "";
        
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<BrandEntity>();
        if(key != null && !key.trim().isEmpty()) {
            wrapper.and((obj) -> {
                obj.eq("brand_id", key).or().like("name", key);
            });
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


    }