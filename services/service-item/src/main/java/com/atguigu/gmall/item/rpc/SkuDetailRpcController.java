package com.atguigu.gmall.item.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.item.vo.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.spc
 * @ClassName : SkuDatailRpcController.java
 * @createTime : 2022/11/12 14:12
 * @Description :
 */
@RequestMapping("/api/inner/rpc/item/")
@RestController
public class SkuDetailRpcController {

    @Autowired
    private SkuDetailService skuDetailService;

    @GetMapping("sku/detail/{skuId}")
    public Result<SkuDetailVo> getSkuDetail(@PathVariable("skuId")Long skuId){
        //TODO 查询商品详情要用的所有数据

        SkuDetailVo skuDetailVo=skuDetailService.getSkuDetail(skuId);
        //每个商品得浏览量热度+1  累计更新  100
        skuDetailService.upDateHotScore(skuId);
        return Result.ok(skuDetailVo);
    }
}
