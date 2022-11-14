package com.atguigu.gmall.item.service.impl;
import java.math.BigDecimal;
import java.util.List;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuFeignClient;
import com.atguigu.gmall.item.service.CacheService;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.SkuInfo;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.item.vo.SkuDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.service.impl
 * @ClassName : SkuDetailServiceiImpl.java
 * @createTime : 2022/11/12 14:56
 * @Description :
 */
@Slf4j
@Service
public class SkuDetailServiceiImpl implements SkuDetailService {
    @Autowired
    SkuFeignClient skuFeignClient;
    @Autowired
    CacheService cacheService;

    /**
     * 缓存穿透：随机值穿透攻击
     * 缓存击穿
     * 缓存雪崩
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {

        String cacheKey = "sku:info:"+skuId;
        //1、先去缓存中查询商品
        SkuDetailVo cacheData= cacheService.getCacheData(cacheKey);
        //使用【bitmap解决随机值穿透攻击】对数据库所有商品进行占位，如果缓存没有这个数据，
        // 可以先看bitmap有没有，如果有就放给数据库，没有则直接打回
        if (cacheData==null){
            log.info("[]商品，缓存未命中，准备回源",skuId);
            //使用bitmap防止穿透攻击
            boolean f=cacheService.existSkuIdByBitMap(skuId);

            if (f) {
                log.info("[]商品，位图判定有，正在查询。。。",skuId);
                //2、bitmap中有，说明数据库有，只是缓存没有
                //3、缓存没有，需要回源
                SkuDetailVo skuDetailVo = getSkuDetailVoFormRpc(skuId);
                //4、保存数据，不管数据库有没有，无条件保存：null值缓存，解决一般的缓存穿透问题
                cacheService.saveCacheData(cacheKey,skuDetailVo);
                return skuDetailVo;
            }

            log.info("[]商品，位图判定没有，有隐藏攻击风险，直接返回null",skuId);
            //5、说明bitmap中没有，直接返回null
            return null;
        }

        log.info("[]商品，缓存命中，直接返回",skuId);
        return cacheData;
    }
    /**
     * 远程查询商品详情
     * @param skuId
     * @return
     */
    private SkuDetailVo getSkuDetailVoFormRpc(Long skuId) {
        SkuDetailVo skuDetailVo=new SkuDetailVo();
        //基本信息
        SkuInfo skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
        //商品图片
        List<SkuImage> skuImages = skuFeignClient.getSkuImages(skuId).getData();
        skuInfo.setSkuImageList(skuImages);
        //1、sku的基本信息,以及图片
        skuDetailVo.setSkuInfo(skuInfo);
        //sku所在的完整分类
        CategoryView data1 = skuFeignClient.getSkuCategoryView(skuInfo.getCategory3Id()).getData();
        skuDetailVo.setCategoryView(data1);
        //3、sku对应的spu定义的所有销售属性列表
        List<SpuSaleAttr> data = skuFeignClient.getSpuSaleAttrAndValue(skuInfo.getSpuId()).getData();
        skuDetailVo.setSpuSaleAttrList(data);


        //4、sku各种组合的json
        String skuValueJson = skuFeignClient.getSkuValueJson(skuInfo.getSpuId()).getData();
        skuDetailVo.setValuesSkuJson(skuValueJson);

        //查询商品价格
        Result<BigDecimal> skuPrice = skuFeignClient.getSkuPrice(skuId);
        skuDetailVo.setPrice(skuPrice.getData());
        return skuDetailVo;
    }
}
