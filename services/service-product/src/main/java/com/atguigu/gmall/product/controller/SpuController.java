package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.SpuImage;
import com.atguigu.gmall.product.entity.SpuInfo;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.vo.SpuInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.controller
 * @ClassName : SpuController.java
 * @createTime : 2022/11/10 11:07
 * @Description :
 */
@Api(tags = "Spu管理")
@RestController
@RequestMapping("/admin/product/")
public class SpuController {

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private SpuImageService spuImageService;
    /**
     *
     * @param ps
     * @param pn
     * @param c3Id
     * @return
     */
    @ApiOperation("分页查询SPU列表")
    @GetMapping("{page}/{limit}")
    public Result spuList(@ApiParam("每页大小")@PathVariable("limit") Long ps,
                          @ApiParam("页码") @PathVariable("page") Long pn,
                          @ApiParam("分类Id")@RequestParam("category3Id")Long c3Id){

        Page<SpuInfo> page = new Page<>(pn,ps);
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3-id",c3Id);
        Page<SpuInfo> result = spuInfoService.page(page,wrapper);

        return Result.ok(result);
    }

    /**
     * SPU商品保存
     * @param spuInfoVo
     * @return
     */
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfoVo spuInfoVo){
        spuInfoService.saveSpuInfo(spuInfoVo);
        return Result.ok();
    }

    /**
     * 查询所有spu的所有图片
     * @param spuId
     * @return
     */
    @GetMapping("spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId") Long spuId){
        QueryWrapper<SpuImage> wrapper=new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);

        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }
}
