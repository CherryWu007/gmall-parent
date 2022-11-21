package com.atguigu.gmall.feignclients.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.feignclients.cart
 * @ClassName : CartFeignClient.java
 * @createTime : 2022/11/21 18:20
 * @Description :
 */
@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {

    @GetMapping("/add/{skuId}/{skuNum}")
    Result<SkuInfo> add(@PathVariable Long skuId, @PathVariable Integer skuNum);

    @PostMapping("/delete")
    Result deleteChecked();
}
