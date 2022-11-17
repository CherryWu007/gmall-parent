package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.cache.service.CacheService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.cache.annotation.MallCache;
import com.atguigu.gmall.feignclients.product.SkuFeignClient;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.item.vo.SkuDetailVo;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.service.impl
 * @ClassName : SkuDetailServiceImpl.java
 * @createTime : 2022/11/12 14:56
 * @Description :
 */
@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    @Autowired
    SkuFeignClient skuFeignClient;
    @Autowired
    CacheService cacheService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ThreadPoolExecutor executor;



    /**
     * 1、缓存的key：指定成一个表达式。这样才能适配所有场景
     * 2、是否要用bitmap：不一定；
     *      菜单：三级分类；就那一条数据无需bitmap
     *      详情： skuids
     *      订单： orderids
     *      物流： wlids
     * @param skuId
     * @return
     */
    @Override
    @MallCache(
            cacheKey = RedisConst.SKU_INFO_CACHE_KEY+"#{#args[0]}",
            bitmapIndex = "#{#args[0]}",
            bitmapKey = RedisConst.SKUID_BITMAP_KEY,
            ttl = 7,
            unit = TimeUnit.DAYS
    )//以后专注业务逻辑
    public SkuDetailVo getSkuDetail(Long skuId) {
        return getSkuDetailVoFormRpc(skuId);
    }

    /*
    分布式未提取之前
    public SkuDetailVo getSkuDetailWithRedisson(Long skuId) {
        String cacheKey = RedisConst.SKU_INFO_CACHE_KEY +skuId;
        //1、先去缓存中查询商品
        SkuDetailVo cacheData= cacheService.getCacheData(cacheKey);
        //使用【bitmap解决随机值穿透攻击】对数据库所有商品进行占位，如果缓存没有这个数据，
        // 可以先看bitmap有没有，如果有就放给数据库，没有则直接打回
        if (cacheData==null){
            log.info("[]商品，缓存未命中，准备回源",skuId);
            //使用bitmap防止穿透攻击
            boolean f=cacheService.existSkuIdByBitMap(skuId);
            if (f) {
                //容易出现击穿风险：加锁解决
                SkuDetailVo skuDetailVo =null;
                //lock:sku:info:50//锁的粒度要细
                String lockKey = RedisConst.LOCK_PREFIX+cacheKey;
                RLock lock = redissonClient.getLock(lockKey);
                //100w并发同时进来，只有一个true，其余都立即false
                boolean b = lock.tryLock();
                if (b){
                    try {
                        //每个人都要得到锁：最后一个得锁的人会等的超级久
                        //判断出哪些得到锁，哪些没有，得到锁的人查库，没有等到的等待一阵去差缓存
                        log.info("[]商品，位图判定有，正在查询。。。",skuId);
                        //2、bitmap中有，说明数据库有，只是缓存没有
                        //3、缓存没有，需要回源
                        skuDetailVo = getSkuDetailVoFormRpc(skuId);
                        //4、保存数据，不管数据库有没有，无条件保存：null值缓存，解决一般的缓存穿透问题
                        cacheService.saveCacheData(cacheKey,skuDetailVo);
                    } finally {
                        lock.unlock();
                    }
                }else {
                    try {
                        Thread.sleep(500);
                        //5、直接查缓存
                        return cacheData=cacheService.getCacheData(cacheKey);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                lock.unlock();
                return skuDetailVo;
            }
            log.info("[]商品，位图判定没有，有隐藏攻击风险，直接返回null",skuId);
            //5、说明bitmap中没有，直接返回null
            return null;
        }
        log.info("[]商品，缓存命中，直接返回",skuId);
        return cacheData;
    }*/
    /**
     * 远程查询商品详情
     * @param skuId
     * @return
     */
    private SkuDetailVo getSkuDetailVoFormRpc(Long skuId) {
        //异步
        SkuDetailVo skuDetailVo=new SkuDetailVo();
        //基本信息
        CompletableFuture<SkuInfo> skuInfoAsync=CompletableFuture.supplyAsync(()->{
            SkuInfo skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
            return skuInfo;
        },executor);

        //商品图片
        CompletableFuture<Void> imageAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            List<SkuImage> skuImages = skuFeignClient.getSkuImages(skuId).getData();
            skuInfo.setSkuImageList(skuImages);
            //1、sku的基本信息,以及图片
            skuDetailVo.setSkuInfo(skuInfo);
        }, executor);

        //3sku所在的完整分类
        CompletableFuture<Void> categoryAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            CategoryView data1 = skuFeignClient.getSkuCategoryView(skuInfo.getCategory3Id()).getData();
            skuDetailVo.setCategoryView(data1);
        }, executor);

        //4、sku对应的spu定义的所有销售属性列表
        CompletableFuture<Void> saleAttrAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            List<SpuSaleAttr> data = skuFeignClient.getSpuSaleAttrAndValue(skuInfo.getSpuId()).getData();
            skuDetailVo.setSpuSaleAttrList(data);
        }, executor);


        //5、sku各种组合的json
        CompletableFuture<Void> skuJsonAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            String skuValueJson = skuFeignClient.getSkuValueJson(skuInfo.getSpuId()).getData();
            skuDetailVo.setValuesSkuJson(skuValueJson);
        }, executor);


        //查询商品价格
        CompletableFuture<Void> priceAsync = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> skuPrice = skuFeignClient.getSkuPrice(skuId);
            skuDetailVo.setPrice(skuPrice.getData());
        },executor);

        CompletableFuture.allOf(imageAsync,categoryAsync,saleAttrAsync,skuJsonAsync,priceAsync).join();
        return skuDetailVo;
    }

        /*private SkuDetailVo getSkuDetailVoFromRpc(Long skuId){
        //new Thread().start(); //直接new Thread。导致由原来的1个请求裂变为6个Thread；内存cpu都顶不住

        //接收所有商品信息
        SkuDetailVo vo = new SkuDetailVo();

        //1.skuInfo:{id,skuName,skuDefaultImg,skuImageList:{imgUrl},weight,spuId } 基本信息
        CompletableFuture<SkuInfo> skuInfoAsync = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = skuItemFeignClient.getSkuInfo(skuId).getData();
            vo.setSkuInfo(skuInfo);
            return skuInfo;
        }, executor);


        //2.categoryView :{category1Id,category1Name,category2Id,category2Name,category3Id,category3Name} sku分类信息
        CompletableFuture<Void> categorysAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            vo.setCategoryView(skuItemFeignClient.getCategoryView(skuId).getData());
        }, executor);

        //3.spuSaleAttrList 当前sku所属spu的销售属性
        CompletableFuture<Void> saleAttrAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            vo.setSpuSaleAttrList(skuItemFeignClient.getSpuSaleAttrList(skuId).getData());
        }, executor);

        //4.valuesSkuJson  sku各种组合的json

        CompletableFuture<Void> skuJsonAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {

            List<SkuValueJson> skuValueJsonList = skuItemFeignClient.getSkuValueJson(skuId).getData();
            Map<String,Long> map = new HashMap();
            for (SkuValueJson skuValueJson:skuValueJsonList){
                map.put(skuValueJson.getVal(),skuValueJson.getSkuId());
            }
            String jsonString = JSON.toJSONString(map);
            vo.setValuesSkuJson(jsonString);

        }, executor);


        //5.price
        CompletableFuture<Void> priceAsync = CompletableFuture.runAsync(() -> {
            BigDecimal skuPrice = skuItemFeignClient.getPrice(skuId).getData();
            vo.setPrice(skuPrice);
        }, executor);

        CompletableFuture.allOf(categorysAsync,saleAttrAsync,skuJsonAsync,priceAsync).join();
        return vo;
    }*/


}
