package com.nebulamall.coupon.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebulamall.coupon.entity.SpuBoundsEntity;
import com.nebulamall.coupon.service.SpuBoundsService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 商品spu积分设置
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 17:59:25
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService smsSpuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:smsspubounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsSpuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:smsspubounds:info")
    public R info(@PathVariable("id") Long id){
		SpuBoundsEntity smsSpuBounds = smsSpuBoundsService.getById(id);

        return R.ok().put("smsSpuBounds", smsSpuBounds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:smsspubounds:save")
    public R save(@RequestBody SpuBoundsEntity smsSpuBounds){
		smsSpuBoundsService.save(smsSpuBounds);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:smsspubounds:update")
    public R update(@RequestBody SpuBoundsEntity smsSpuBounds){
		smsSpuBoundsService.updateById(smsSpuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:smsspubounds:delete")
    public R delete(@RequestBody Long[] ids){
		smsSpuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
