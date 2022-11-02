package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.SkuInfo;

import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.vo.SkuInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.controller
 * @ClassName : SkuController.java
 * @createTime : 2022/11/10 15:29
 * @Description :
 */
@RequestMapping("/admin/product/")
@RestController
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("list/{pn}/{ps}")
    public Result list(@PathVariable("pn") Long pn, @PathVariable("ps") Long ps){

        Page<SkuInfo> skuInfoPage = new Page<>(pn, ps);
        Page<SkuInfo> page = skuInfoService.page(skuInfoPage);

        return Result.ok(page);
    }

    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfoVo vo){
        skuInfoService.saveSkuInfo(vo);
        return Result.ok();
    }
}
