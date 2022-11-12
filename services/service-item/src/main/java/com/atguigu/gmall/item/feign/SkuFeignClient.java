package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.feign
 * @ClassName : SkuFeignClient.java
 * @createTime : 2022/11/12 15:49
 * @Description :
 */
@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product/")
public interface SkuFeignClient {

    /**
     * 1、查询sku_info
     * @param skuId
     * @return
     */
    @GetMapping("skuinfo/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId);

    /**
     * 查询sku所有图片
     * @param skuId
     * @return
     */
    @GetMapping("skuinfo/{skuId}")
    Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId);

    /**
     * 查询某个sku对应的spu定义的所有销售属性名和值
     * @param spuId
     * @return
     */
    @GetMapping("skuinfo/spu/saleAttrandvalue/{spuId}")
    Result<List<SpuSaleAttr>> getSpuSaleAttrAndValue(@PathVariable("spuId") Long spuId);

    /**
     * 查询某个sku对应的分类的完整路径
     * @param c3Id
     * @return
     */
    @GetMapping("skuinfo/categoryview/{c3Id}")
    public Result<CategoryView> getSkuCategoryView(@PathVariable("c3Id")Long c3Id);

    /**
     * 查询某个sku对应的spu定义的所有sku的销售属性值组合
     * @param spuId
     * @return
     */
    @GetMapping("skuInfo/valuejson/{spuId}")
    Result<String> getSkuValueJson(@PathVariable("spuId") Long spuId);
}
