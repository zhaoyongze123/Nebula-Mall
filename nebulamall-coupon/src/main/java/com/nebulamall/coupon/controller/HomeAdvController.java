package com.nebulamall.coupon.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebulamall.coupon.entity.HomeAdvEntity;
import com.nebulamall.coupon.service.HomeAdvService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 首页轮播广告
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 17:59:24
 */
@RestController
@RequestMapping("coupon/homeadv")
public class HomeAdvController {
    @Autowired
    private HomeAdvService smsHomeAdvService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:smshomeadv:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsHomeAdvService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:smshomeadv:info")
    public R info(@PathVariable("id") Long id){
		HomeAdvEntity smsHomeAdv = smsHomeAdvService.getById(id);

        return R.ok().put("smsHomeAdv", smsHomeAdv);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:smshomeadv:save")
    public R save(@RequestBody HomeAdvEntity smsHomeAdv){
		smsHomeAdvService.save(smsHomeAdv);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:smshomeadv:update")
    public R update(@RequestBody HomeAdvEntity smsHomeAdv){
		smsHomeAdvService.updateById(smsHomeAdv);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:smshomeadv:delete")
    public R delete(@RequestBody Long[] ids){
		smsHomeAdvService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
