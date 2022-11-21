package com.atguigu.gmall.weball.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feignclients.cart.CartFeignClient;
import com.atguigu.gmall.product.entity.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.controller
 * @ClassName : CartController.java
 * @createTime : 2022/11/21 18:54
 * @Description :购物车功能控制器
 */
@Controller

public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;

    /**
     * 任何容器类，如果长期一直在内存中，就一定要看考虑，容器内部的元素用完就删
     */
    public static Map<Thread,HttpServletRequest> threadInfo=new ConcurrentHashMap<>();

    @GetMapping("/cart.html")
    public String cart(){
        return "cart/index";
    }

    @GetMapping("/addCart.html")
    public String add(@RequestParam("skuId") Long skuId,
                      @RequestParam("skuNum")Integer skuNum,
                      Model model,
                      HttpServletRequest request){
        threadInfo.put(Thread.currentThread(),request);
        //远程调用购物车
        String tempid = request.getHeader("tempid");
        //远程调用购物车服务，商品加入购物车
        // 这次远程调用由于拦截器放好了之前这个request头里面的数据，所以对方收到的请求头中又用户信息
        Result<SkuInfo> add = cartFeignClient.add(skuId, skuNum);
        //用完就删，就不会OOM
        threadInfo.remove(Thread.currentThread());
        SkuInfo skuInfo = add.getData();
        model.addAttribute("skuNum",skuNum);
        model.addAttribute("skuInfo",skuInfo);
        return "/cart/addCart";
    }

    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){
        cartFeignClient.deleteChecked();
        return "redirect:http://cart.gmall.com/cart.html";
    }
}
