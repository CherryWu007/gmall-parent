package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.rpc
 * @ClassName : SkuRpcController.java
 * @createTime : 2022/11/12 15:15
 * @Description :sku的所有暴露出来的功能
 */
@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuRpcController {
    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    @Autowired
    BaseCategory1Service baseCategory1Service;

    /**
     * 1、查询sku_info
     * @param skuId
     * @return
     */
    @GetMapping("skuinfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId){

        SkuInfo skuInfo=skuInfoService.getById(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 查询sku所有图片
     * @param skuId
     * @return
     */
    @GetMapping("skuinfo/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId){
        QueryWrapper<SkuImage> wrapper=new QueryWrapper<>();
        wrapper.eq("sku_id",skuId);
        List<SkuImage> skuImages = skuImageService.list(wrapper);
        return Result.ok(skuImages);
    }

    /**
     * 查询某个sku对应的spu定义的所有销售属性名和值
     * @param spuId
     * @return
     */
    @GetMapping("skuinfo/spu/saleAttrandvalue/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttr(@PathVariable("spuId") Long spuId,@RequestParam("skuId") Long skuId){

        List<SpuSaleAttr> saleAttrs= spuSaleAttrService.getSpuSaleAttrAndValue(spuId,skuId);

        return Result.ok(saleAttrs);

    }

    /**
     * 查询某个sku对应的分类的完整路径
     * @param skuId
     * @return
     */
    //1.categoryView :{category1Id,category1Name,category2Id,category2Name,category3Id,category3Name}
    @GetMapping("getCategoryView/{skuId}")
    public Result getCategoryView(@PathVariable("skuId") Long skuId){
        Long c3Id = skuInfoService.getById(skuId).getCategory3Id();
        CategoryView categoryView = baseCategory1Service.getCategoryView(c3Id);
        return Result.ok(categoryView);
    }

    /**
     * 查询某个sku对应的spu定义的所有sku的销售属性值组合
     * @param spuId
     * @return
     */
    @GetMapping("skuInfo/valuejson/{spuId}")
    public Result<String> getSkuValueJson(@PathVariable("spuId") Long spuId){

        String json= spuSaleAttrService.getSkuValueJson(spuId);

        return Result.ok(json);
    }

    /**
     * 查询商品价格
     * @param skuId
     * @return
     */
    @GetMapping("skuInfo/price/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable("skuId")Long skuId){
        BigDecimal price=  skuInfoService.getSkuRealTimePrice(skuId);
        return Result.ok(price);
    }
}
