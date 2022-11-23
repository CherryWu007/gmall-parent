package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.entity.CartInfo;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.CartUtils;
import com.atguigu.gmall.common.util.ToJSON;
import com.atguigu.gmall.feignclients.product.SkuFeignClient;
import com.atguigu.gmall.product.entity.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cart.service.impl
 * @ClassName : CartServiceImpl.java
 * @createTime : 2022/11/21 18:36
 * @Description :
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 新增或者修改
     * @param redisCartKey
     * @param skuId
     * @param skuNum
     * @return
     */
    @Override
    public SkuInfo add(String redisCartKey, Long skuId, Integer skuNum) {
        SkuInfo skuInfo = null;

        //给购物车中添加商品。 如果购物车之前没有这个商禹就是新增。如果有就是修改数量
        // 1、以后操作redis都是string， key hk hv 都是字符串
        //查看redis是否有这个商品
        Boolean aBoolean = redisTemplate.opsForHash().hasKey(redisCartKey,skuId.toString());
        //没有就是新增
        if (!aBoolean){
            CartInfo cartInfo = new CartInfo();
            //调用远程商品服务 根据skuid获取商品详情
            skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
            //共享数据到redis
            saveCart(redisCartKey,cartInfo);
        }else{
            //购物车中有这个商品修改商品数量
            //获取单个商品
            CartInfo cartInfo = getCartItem(redisCartKey,skuId.toString());
            cartInfo.setSkuNum(skuNum + cartInfo.getSkuNum());
            //共享到redis
            saveCart(redisCartKey,cartInfo);
            //返回前端需要的商品默认图片和名字
            skuInfo = new SkuInfo();
            skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());
            skuInfo.setSkuName(cartInfo.getSkuName());
            return skuInfo;
        }
        return skuInfo;
    }

    //获取列表
    @Override
    public List<CartInfo> cartList() {
        //获取决定key
        String key = decisionRedisKey();
        List<CartInfo> cartInfoList = getCartInfoList(key);
        return cartInfoList;
    }
    //获取购物车列表
    private List<CartInfo> getCartInfoList(String key) {
        //判断是否有用户id
        String tempId = CartUtils.getTempId();
        Long uid = CartUtils.getUid();
        if (uid!=null){
            //如果有uid代表登陆了 查看 临时id 的购物车是否有商品
            List<Object> values = redisTemplate.opsForHash().values(RedisConst.CART_KEY + tempId);
            //有商品的话直接流式计算为购物车商品项
            if (values != null && values.size() > 0) {
                values.stream().forEach(item->{
                    CartInfo cartInfo = ToJSON.toObject(item.toString(), CartInfo.class);
                    //合并
                    add(key,cartInfo.getSkuId(),cartInfo.getSkuNum());
                });
                //删除临时id的购物车数据
                redisTemplate.delete(RedisConst.CART_KEY + tempId);
            }
        }
        //根据key获取redis商品集合
        List<Object> values = redisTemplate.opsForHash().values(key);
        //遍历商品转换成购物车内商品
        //流式计算将获取到的redis内的对应此id的商品信息的json串转换为实体类的对象集合
        List<CartInfo> cartInfoList = values.stream()
                .map(item -> ToJSON.toObject(item.toString(), CartInfo.class))
                .sorted(Comparator.comparing(CartInfo::getCreateTime))
                .collect(Collectors.toList());
        //异步同步价格
        CompletableFuture.runAsync(()->{
            this.synchronizationPrice(key,cartInfoList);
        },executor);
        return cartInfoList;
    }

    private void synchronizationPrice(String key, List<CartInfo> cartInfoList) {
        //遍历购物车集合  使用流式计算  远程查询数据库的价格和当前redis价格对比,不一样修改价格同步redis
        cartInfoList.stream().forEach(item->{
            Result<BigDecimal> skuPrice = skuFeignClient.getSkuPrice(item.getSkuId());
            if ((skuPrice.getData().subtract(item.getSkuPrice()).doubleValue() > 0.001)){
                log.info("开始同步价格");
                item.setSkuPrice(skuPrice.getData());
                saveCart(key,item);
            }
        });
    }


    //购物车列表页面添加商品
    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        String key = decisionRedisKey();
        //修改数量
        updateCartItem(key,skuId,skuNum);
    }
    //修改选中状态
    @Override
    public void checkCartStatus(Long skuId, Integer status) {
        String key = decisionRedisKey();
        CartInfo cartItem = getCartItem(key, skuId.toString());
        cartItem.setIsChecked(status);
        saveCart(key,cartItem);
    }
    //删除对应skuId的商品
    @Override
    public void deleteCart(Long skuId) {
        String key = decisionRedisKey();
        redisTemplate.opsForHash().delete(key,skuId.toString());
    }
    //删除选中的商品
    @Override
    public void deleteChecked() {
        String key = decisionRedisKey();
        List<CartInfo> cartInfoList = getCartInfoList(key);
        //cartInfoList.stream().filter(item -> item.getIsChecked() == 1)
        //        .forEach(item->redisTemplate.opsForHash().delete(key,item.getSkuId()));
        List<String> collect = cartInfoList.stream().filter(item -> item.getIsChecked() == 1)
                .map(item -> item.getSkuId().toString())
                .collect(Collectors.toList());
        //非空判断
        if (collect!=null && collect.size() > 0){
            redisTemplate.opsForHash().delete(key,collect.toArray());
        }
    }



    //修改商品数量
    private void updateCartItem(String key, Long skuId, Integer skuNum) {
        //获取对应redisKey的数据
        CartInfo cartInfo = getCartItem(key, skuId.toString());
        cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
        saveCart(key,cartInfo);
    }

    //获取单个商品项目
    private CartInfo getCartItem(String redisCartKey, String skuId) {
        String cartJson = (String) redisTemplate.opsForHash().get(redisCartKey, skuId);
        return ToJSON.toObject(cartJson,CartInfo.class);
    }

    /**
     * 共享数据到redis
     * @param redisCartKey
     * @param cartInfo
     */
    private void saveCart(String redisCartKey, CartInfo cartInfo) {
        //新增到redis
        redisTemplate.opsForHash().put(redisCartKey, cartInfo.getSkuId().toString(), ToJSON.toJson(cartInfo));
        //判断当前redisKey是否和临时id的redisKey一样  一样的话设置过期时间
        if (redisCartKey.equals(RedisConst.CART_KEY + CartUtils.getTempId())){
            Long expire = redisTemplate.getExpire(redisCartKey);
            if (expire<0){
                redisTemplate.expire(redisCartKey, Duration.ofDays(RedisConst.TEMPID_TTL));
            }
        }
    }

    /**
     * 决定用购物车哪个键
     * 1、如果只带了tempid; 就是临时购物车
     * 2、如果只带了token; 就是用户购物车
     * 3、如果两个都带了，优先以用户购物车为准;如果临时购物车有商品，还要合并到用户购物车中
     * @return
     */
    @Override
    public String decisionRedisKey() {
        String redisKey = "";
        Long uid = CartUtils.getUid();
        //如果有用户id，一定先用用户的
        if (uid != null) {
            redisKey = RedisConst.CART_KEY + uid;
            return redisKey;
        }
        //是否有临时的
        String tempId = CartUtils.getTempId();
        if (!StringUtils.isEmpty(tempId)) {
            redisKey = RedisConst.CART_KEY + tempId;
        }
        return redisKey;
    }
}
