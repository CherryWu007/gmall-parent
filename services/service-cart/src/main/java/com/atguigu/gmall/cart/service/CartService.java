package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.entity.CartInfo;
import com.atguigu.gmall.product.entity.SkuInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cart.service
 * @ClassName : CartService.java
 * @createTime : 2022/11/21 18:36
 * @Description :
 */

public interface CartService {
    SkuInfo add(String redisCartKey, Long skuId, Integer skuNum);

    String decisionRedisKey();

    List<CartInfo> cartList();

    void addToCart(Long skuId, Integer skuNum);

    void checkCartStatus(Long skuId, Integer status);

    void deleteCart(Long skuId);

    void deleteChecked();

}
