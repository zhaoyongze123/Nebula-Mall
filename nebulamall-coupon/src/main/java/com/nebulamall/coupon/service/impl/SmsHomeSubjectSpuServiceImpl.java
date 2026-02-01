package com.nebulamall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.coupon.dao.SmsHomeSubjectSpuDao;
import com.nebulamall.coupon.entity.SmsHomeSubjectSpuEntity;
import com.nebulamall.coupon.service.SmsHomeSubjectSpuService;


@Service("smsHomeSubjectSpuService")
public class SmsHomeSubjectSpuServiceImpl extends ServiceImpl<SmsHomeSubjectSpuDao, SmsHomeSubjectSpuEntity> implements SmsHomeSubjectSpuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsHomeSubjectSpuEntity> page = this.page(
                new Query<SmsHomeSubjectSpuEntity>().getPage(params),
                new QueryWrapper<SmsHomeSubjectSpuEntity>()
        );

        return new PageUtils(page);
    }

}