package com.atguigu.gmall.cart.rpc;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cart.rpc
 * @ClassName : CartRpcController.java
 * @createTime : 2022/11/21 19:01
 * @Description :
 */
@RequestMapping("/api/inner/rpc/cart")
@RestController
public class CartRpcController {

    @Autowired
    private CartService cartService;
    //添加商品到购物车
    @GetMapping("/add/{skuId}/{skuNum}")
    public Result<SkuInfo> add(@PathVariable Long skuId, @PathVariable Integer skuNum){
        //判断当前购物车携带的key
        String redisCartKey = cartService.decisionRedisKey();
        //添加商品
        SkuInfo skuInfo = cartService.add(redisCartKey,skuId,skuNum);
        return Result.ok(skuInfo);
    }

    //删除选中的商品
    @PostMapping("/delete")
    public Result deleteChecked(){
        cartService.deleteChecked();
        return Result.ok();
    }
}
