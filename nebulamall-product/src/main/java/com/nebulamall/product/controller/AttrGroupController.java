package com.nebulamall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.nebulamall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nebulamall.product.entity.AttrGroupEntity;
import com.nebulamall.product.service.AttrGroupService;
import com.nebulamall.product.service.AttrService;
import com.nebulamall.product.dao.AttrAttrgroupRelationDao;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 属性分组
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-01 04:52:39
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    /**
     * 列表
     *
     * 请求参数示例：
     * {
     * page： 1，//当前页码
     * 1imit： 10，//每页记录数
     * sidx：‘id‘，//排序字段
     * order：‘asc/desc‘，//排序方式
     * KEY： ‘华为‘//检索关键字
     * }
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *
     * 请求参数示例：
     * {
     * attrGroupId： 1 //属性分组ID
     * }
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 获取属性分组关联的所有属性列表
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        java.util.List<com.nebulamall.product.vo.AttrGroupRelationAttrVo> attrList = attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data", attrList);
    }
    
    /**
     * 新增属性分组关联
     */
    @RequestMapping("/attr/relation")
    public R addAttrRelation(@RequestBody java.util.List<com.nebulamall.product.vo.AttrGroupRelationDeleteVo> relationList){
        attrService.addRelation(relationList);
        return R.ok();
    }
    /**
     * 获取属性分组没有关联的属性列表
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R noAttrRelation(@PathVariable("attrGroupId") Long attrGroupId
                           , @RequestParam Map<String, Object> params){

        PageUtils page = attrService.getNoRelationAttr(params, attrGroupId);
        return R.ok().put("page", page);
    }

    
    /**
     * 删除属性分组中指定的属性关联
     */
    @RequestMapping("{attrGroupId}/attr/relation/delete")
    public R deleteRelation(@PathVariable("attrGroupId") Long attrGroupId, @RequestBody Long[] attrIds){
        attrService.deleteRelation(attrGroupId, attrIds);
        return R.ok();
    }
    
    /**
     * 删除属性分组关联（支持VO对象列表）
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody java.util.List<com.nebulamall.product.vo.AttrGroupRelationDeleteVo> relationList){
        // 逐个删除属性关联关系
        for (com.nebulamall.product.vo.AttrGroupRelationDeleteVo vo : relationList) {
            relationDao.delete(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.nebulamall.product.entity.AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", vo.getAttrGroupId())
                    .eq("attr_id", vo.getAttrId())
            );
        }
        return R.ok();
    }

    /*
        * 获取属性分组及其关联的属性列表
     */
    //Request URL
    //http://localhost:8888/api/product/attrgroup/225/withattr?t=1770818022301
    //Request Method
    //GET

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data", vos);
    }

}
