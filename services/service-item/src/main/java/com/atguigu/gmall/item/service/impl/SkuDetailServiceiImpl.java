package com.atguigu.gmall.item.service.impl;
import java.math.BigDecimal;
import java.util.List;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuFeignClient;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.google.common.collect.Lists;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.SkuInfo;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.item.vo.SkuDetailVo;
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
@Service
public class SkuDetailServiceiImpl implements SkuDetailService {
    @Autowired
    SkuFeignClient skuFeignClient;

    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {

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
