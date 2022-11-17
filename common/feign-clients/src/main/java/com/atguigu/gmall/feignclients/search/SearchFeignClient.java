package com.atguigu.gmall.feignclients.search;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.feign
 * @ClassName : SearchFeignClient.java
 * @createTime : 2022/11/17 15:52
 * @Description :
 */
@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {

    /**
     * 删除
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    Result deleteGoods(@PathVariable Long skuId);

    /**
     * 保存
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    Result saveGoods(@RequestBody Goods goods);
    /**
     * 搜索
     * @param vo
     * @return
     */
    @PostMapping("/goods")
    Result<SearchRespVo> searchGoods(@RequestBody SearchParamVo vo);
}
