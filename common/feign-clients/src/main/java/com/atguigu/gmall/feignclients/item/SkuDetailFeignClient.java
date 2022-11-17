package com.atguigu.gmall.feignclients.item;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.vo.SkuDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.feign
 * @ClassName : SkuDetailFeignClient.java
 * @createTime : 2022/11/12 14:22
 * @Description :
 */
@RequestMapping("/api/inner/rpc/item/")
@FeignClient("service-item")
public interface SkuDetailFeignClient {
    /**
     * controller:从注解获取数据
     * @RequestParam:从请求中获取数据
     * @PathVariable：从请求路径中获取数据
     * @RequestBody：从请求体里获取数据
     * @RequestHeader：从请求头获取
     * 。。。
     * FeignClient：把这些数据放到哪里，并发出去
     *      @RequestParam:把方法传来的参数放到请求参数位置
     *      @PathVariable：把方法传来的参数放到请求路径
     *      @RequestBody：把方法传来的参数转成json放到请求体
     *      @RequestHeader：把方法传来的参数放到请求头
     *      。。。
     * @param skuId
     * @return
     */
    //@GetMapping("/api/inner/rpc/item/sku/detail/{skuId}")
    //Result getSkuDetail(@PathVariable("skuId") Long skuId);

    @GetMapping("sku/detail/{skuId}")
    public Result<SkuDetailVo> getSkuDetail(@PathVariable("skuId")Long skuId);

}
