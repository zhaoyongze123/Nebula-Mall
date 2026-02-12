package com.nebulamall.product.service.impl;

import com.nebulamall.product.entity.*;
import com.nebulamall.product.feign.CouponFeignService;
import com.nebulamall.product.service.*;
import com.nebulamall.product.vo.*;
import com.common.to.SpuBoundTo;
import com.common.to.SkuReductionTo;
import com.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.nebulamall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存完整的SPU信息
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        System.err.println("\n\n========================================");
        System.err.println("⚡⚡⚡ 开始保存SPU信息，SPU名称: " + vo.getSpuName());
        System.err.println("========================================\n");
        
        try {
            log.warn("========================================");
            log.warn("开始保存SPU信息，SPU名称: {}", vo.getSpuName());
            log.warn("========================================");
            
            //1、保存spu基本信息 pms_spu_info
            log.warn("【第1步】保存SPU基本信息 pms_spu_info");
            System.err.println("→ 第1步：开始保存SPU基本信息");
            SpuInfoEntity infoEntity = new SpuInfoEntity();
            BeanUtils.copyProperties(vo,infoEntity);
            infoEntity.setCreateTime(new Date());
            infoEntity.setUpdateTime(new Date());
            
            System.err.println("  - 执行 saveBaseSpuInfo 前");
            this.saveBaseSpuInfo(infoEntity);
            System.err.println("  - 执行 saveBaseSpuInfo 后，ID: " + infoEntity.getId());
            
            Long spuId = infoEntity.getId();
            
            System.err.println("  - 检查 spuId: " + spuId);
            if(spuId == null || spuId <= 0) {
                System.err.println("❌ SPU保存失败，ID为空!");
                log.error("❌ SPU保存失败，ID为空!");
                throw new RuntimeException("SPU保存失败，ID为空");
            }
            System.err.println("✓ SPU基本信息保存成功，SPU ID: " + spuId);
            log.warn("✓ SPU基本信息保存成功，SPU ID: {}", spuId);

            //2、保存Spu的描述图片 pms_spu_info_desc
            System.err.println("→ 第2步：开始保存SPU描述图片");
            log.warn("【第2步】保存SPU描述图片 pms_spu_info_desc");
            List<String> decript = vo.getDecript();
            if(decript != null && !decript.isEmpty()){
                System.err.println("  - 描述图片数: " + decript.size());
                SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
                descEntity.setSpuId(spuId);
                descEntity.setDecript(String.join(",",decript));
                spuInfoDescService.saveSpuInfoDesc(descEntity);
                System.err.println("✓ SPU描述图片保存成功");
                log.warn("✓ SPU描述图片保存成功，图片数: {}", decript.size());
            } else {
                System.err.println("⚠ SPU描述图片为空");
                log.warn("⚠ SPU描述图片为空");
            }

            //3、保存spu的图片集 pms_spu_images
            System.err.println("→ 第3步：开始保存SPU展示图片");
            log.warn("【第3步】保存SPU展示图片 pms_spu_images");
            List<String> images = vo.getImages();
            if(images != null && !images.isEmpty()){
                System.err.println("  - 展示图片数: " + images.size());
                imagesService.saveImages(spuId, images);
                System.err.println("✓ SPU展示图片保存成功");
                log.warn("✓ SPU展示图片保存成功，图片数: {}", images.size());
            } else {
                System.err.println("⚠ SPU展示图片为空");
                log.warn("⚠ SPU展示图片为空");
            }

            //4、保存spu的规格参数;pms_product_attr_value
            System.err.println("→ 第4步：开始保存SPU规格参数");
            log.warn("【第4步】保存SPU规格参数 pms_product_attr_value");
            List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
            if(baseAttrs != null && !baseAttrs.isEmpty()){
                System.err.println("  - 规格参数数: " + baseAttrs.size());
                List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
                    ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
                    valueEntity.setAttrId(attr.getAttrId());
                    AttrEntity attrEntity = attrService.getById(attr.getAttrId());
                    if(attrEntity == null) {
                        throw new RuntimeException("属性ID不存在: " + attr.getAttrId());
                    }
                    valueEntity.setAttrName(attrEntity.getAttrName());
                    valueEntity.setAttrValue(attr.getAttrValues());
                    valueEntity.setQuickShow(attr.getShowDesc());
                    valueEntity.setSpuId(spuId);
                    return valueEntity;
                }).collect(Collectors.toList());
                attrValueService.saveProductAttr(collect);
                System.err.println("✓ SPU规格参数保存成功");
                log.warn("✓ SPU规格参数保存成功，属性数: {}", collect.size());
            } else {
                System.err.println("⚠ SPU规格参数为空");
                log.warn("⚠ SPU规格参数为空");
            }

            //5、保存spu的积分信息；coupon_spu_bounds
            log.info("【第5步】保存SPU积分信息 coupon_spu_bounds");
            Bounds bounds = vo.getBounds();
            if(bounds != null){
                SpuBoundTo spuBoundTo = new SpuBoundTo();
                BeanUtils.copyProperties(bounds,spuBoundTo);
                spuBoundTo.setSpuId(spuId);
                try {
                    R r = couponFeignService.saveSpuBounds(spuBoundTo);
                    if(r.getCode() != 0){
                        throw new RuntimeException("远程保存spu积分信息失败");
                    }
                    log.info("✓ SPU积分信息保存成功");
                } catch (Exception e) {
                    log.error("✗ 远程保存spu积分信息异常: {}", e.getMessage(), e);
                    throw e;
                }
            } else {
                log.warn("⚠ SPU积分信息为空");
            }

            //6、保存当前spu对应的所有sku信息
            log.info("【第6步】保存SKU信息");
            List<Skus> skus = vo.getSkus();
            if(skus == null || skus.isEmpty()){
                log.warn("⚠ SKU列表为空");
            } else {
                log.info("待保存SKU数: {}", skus.size());
                
                for(int i = 0; i < skus.size(); i++) {
                    Skus item = skus.get(i);
                    try {
                        log.info("  - 保存第{}/{}个SKU: {}", (i+1), skus.size(), item.getSkuName());
                        
                        String defaultImg = "";
                        List<Images> itemImages = item.getImages();
                        if(itemImages != null && !itemImages.isEmpty()){
                            for (Images image : itemImages) {
                                if(image.getDefaultImg() == 1){
                                    defaultImg = image.getImgUrl();
                                    break;
                                }
                            }
                        }
                        
                        SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                        BeanUtils.copyProperties(item,skuInfoEntity);
                        skuInfoEntity.setBrandId(infoEntity.getBrandId());
                        skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                        skuInfoEntity.setSaleCount(0L);
                        skuInfoEntity.setSpuId(spuId);
                        skuInfoEntity.setSkuDefaultImg(defaultImg);
                        
                        //6.1）、sku的基本信息；pms_sku_info
                        skuInfoService.saveSkuInfo(skuInfoEntity);
                        Long skuId = skuInfoEntity.getSkuId();
                        
                        if(skuId == null || skuId <= 0) {
                            throw new RuntimeException("SKU保存失败，ID为空");
                        }
                        log.info("    ✓ SKU基本信息保存成功，SKU ID: {}", skuId);

                        //6.2）、sku的图片信息；pms_sku_image
                        if(itemImages != null && !itemImages.isEmpty()){
                            List<SkuImagesEntity> imagesEntities = itemImages.stream().map(img -> {
                                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                                skuImagesEntity.setSkuId(skuId);
                                skuImagesEntity.setImgUrl(img.getImgUrl());
                                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                                return skuImagesEntity;
                            }).filter(entity->{
                                return !StringUtils.isEmpty(entity.getImgUrl());
                            }).collect(Collectors.toList());
                            
                            if(!imagesEntities.isEmpty()) {
                                skuImagesService.saveBatch(imagesEntities);
                                log.info("    ✓ SKU图片信息保存成功，图片数: {}", imagesEntities.size());
                            }
                        }

                        //6.3）、sku的销售属性信息：pms_sku_sale_attr_value
                        List<Attr> attr = item.getAttr();
                        if(attr != null && !attr.isEmpty()){
                            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                                SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                                BeanUtils.copyProperties(a, attrValueEntity);
                                attrValueEntity.setSkuId(skuId);
                                return attrValueEntity;
                            }).collect(Collectors.toList());
                            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                            log.info("    ✓ SKU销售属性保存成功，属性数: {}", skuSaleAttrValueEntities.size());
                        }

                        //6.4）、sku的优惠、满减等信息；coupon_sku_ladder\coupon_sku_full_reduction\member_price
                        SkuReductionTo skuReductionTo = new SkuReductionTo();
                        BeanUtils.copyProperties(item,skuReductionTo);
                        skuReductionTo.setSkuId(skuId);
                        if(skuReductionTo.getFullCount() > 0 || (skuReductionTo.getFullPrice() != null && skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1)){
                            try {
                                R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                                if(r1.getCode() != 0){
                                    throw new RuntimeException("远程保存sku优惠信息失败");
                                }
                                log.info("    ✓ SKU优惠信息保存成功");
                            } catch (Exception e) {
                                log.error("    ✗ 远程保存sku优惠信息异常: {}", e.getMessage(), e);
                                throw e;
                            }
                        }
                    } catch (Exception e) {
                        log.error("✗ 保存第{}个SKU失败: {}", (i+1), e.getMessage(), e);
                        throw new RuntimeException("保存SKU信息失败: " + e.getMessage(), e);
                    }
                }
                log.info("✓ 所有SKU信息保存完成");
            }
            
            log.info("========================================");
            log.info("✓ SPU及其相关信息保存完成，SPU ID: {}", spuId);
            log.info("========================================");
        } catch (Exception e) {
            System.err.println("\n❌❌❌ 保存SPU信息异常 ❌❌❌");
            System.err.println("异常信息: " + e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("❌❌❌ 异常结束 ❌❌❌\n");
            
            log.error("========================================");
            log.error("✗ 保存SPU信息失败，原因: {}", e.getMessage(), e);
            log.error("========================================");
            throw new RuntimeException("保存SPU信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * catelogId: 225
         */

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


}