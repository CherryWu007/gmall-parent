package com.atguigu.gmall.weball.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.vo.SkuDetailVo;
import com.atguigu.gmall.weball.feign.SkuDetailFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.controller
 * @ClassName : ItemController.java
 * @createTime : 2022/11/12 13:37
 * @Description :商品详情页
 */
@Controller
public class ItemController {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    /**
     * 查询商品详情
     * @param skuId
     * @return
     */
    @GetMapping("{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId,Model model){

        //TODO 远程调用商品详情服务详细数据
        Result<SkuDetailVo> skuDetail = skuDetailFeignClient.getSkuDetail(skuId);
        SkuDetailVo detailVo = skuDetail.getData();

        //{category1Id,category1Name,category2Id,category2Name,category3Id,category3Name, }
        //当前商品所属的详细数据
        model.addAttribute("categoryView",detailVo.getCategoryView());

        //skuInfo.skuName,id,skuDefaultImage,skuInfoImageList,skuWeight
        model.addAttribute("skuInfo",detailVo.getSkuInfo());

        //3、spuSaleAttrList 拿到当前sku所属的spu当时到底定义了多少销售属性名和值
        model.addAttribute("spuSaleAttrList",detailVo.getSpuSaleAttrList());

        //4、valuesSkuJson:json字符串
        model.addAttribute("valuesSkuJson",detailVo.getValuesSkuJson());

        //5、商品价格
        model.addAttribute("price",detailVo.getPrice());
        return "item/index";
    }
}
