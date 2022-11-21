package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.entity.CartInfo;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cart.controller
 * @ClassName : CartController.java
 * @createTime : 2022/11/21 18:12
 * @Description :购物车功能控制器
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/addCart.html")
    public Result addToCart(@RequestParam("skuId") Long skuId,@RequestParam("skuNum") Integer skuNum){
        try {
            cartService.addToCart(skuId,skuNum);
        } catch (GmallException e) {
            return Result.fail(ResultCodeEnum.CART_SKU_NUM);
        }
        return Result.ok();
    }

    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable Long skuId){
        cartService.deleteCart(skuId);
        return Result.ok();
    }

    /**
     * 修改页面选中状态
     * @param skuId
     * @param status
     * @return
     */
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result checkCartStatus(@PathVariable Long skuId,@PathVariable Integer status){
        cartService.checkCartStatus(skuId,status);
        return Result.ok();
    }

    /**
     * 购物车列表
     * @return
     */
    @GetMapping("/cartList")
    public Result cartList(){
        List<CartInfo> list = cartService.cartList();
        return Result.ok(list);
    }
}
