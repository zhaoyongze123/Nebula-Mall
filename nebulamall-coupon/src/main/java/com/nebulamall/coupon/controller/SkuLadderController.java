package com.nebulamall.coupon.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebulamall.coupon.entity.SkuLadderEntity;
import com.nebulamall.coupon.service.SkuLadderService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 商品阶梯价格
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 17:59:25
 */
@RestController
@RequestMapping("coupon/skuladder")
public class SkuLadderController {
    @Autowired
    private SkuLadderService smsSkuLadderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:smsskuladder:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = smsSkuLadderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:smsskuladder:info")
    public R info(@PathVariable("id") Long id){
		SkuLadderEntity smsSkuLadder = smsSkuLadderService.getById(id);

        return R.ok().put("smsSkuLadder", smsSkuLadder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:smsskuladder:save")
    public R save(@RequestBody SkuLadderEntity smsSkuLadder){
		smsSkuLadderService.save(smsSkuLadder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:smsskuladder:update")
    public R update(@RequestBody SkuLadderEntity smsSkuLadder){
		smsSkuLadderService.updateById(smsSkuLadder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:smsskuladder:delete")
    public R delete(@RequestBody Long[] ids){
		smsSkuLadderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
