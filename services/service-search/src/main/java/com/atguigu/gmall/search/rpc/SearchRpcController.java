package com.atguigu.gmall.search.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.service.SearchService;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.rpc
 * @ClassName : SearchRpcController.java
 * @createTime : 2022/11/17 15:47
 * @Description :
 */
@RestController
@RequestMapping("/api/inner/rpc/search")
public class SearchRpcController {
    @Autowired
    SearchService searchService;

    /**
     * 删除
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    Result deleteGoods(@PathVariable Long skuId){
        searchService.deleteGoods(skuId);
        return Result.ok();
    }

    /**
     * 保存
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    Result saveGoods(@RequestBody Goods goods){
        searchService.saveGoods(goods);
        return Result.ok();
    }


    @PostMapping("/goods")
    public Result<SearchRespVo> searchGoods(@RequestBody SearchParamVo vo){
        //处理检索
        SearchRespVo respVo=searchService.search(vo);
        return Result.ok(respVo);
    }
}
