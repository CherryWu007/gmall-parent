package com.atguigu.gmall.item.service;

import com.atguigu.gmall.item.vo.SkuDetailVo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.service
 * @ClassName : CacheService.java
 * @createTime : 2022/11/13 19:34
 * @Description :
 */

public interface CacheService {
    SkuDetailVo getCacheData(String cacheKey);

    /**
     * 保存数据
     * @param cacheKey
     * @param cacheData
     */
    void saveCacheData(String cacheKey, SkuDetailVo cacheData);

    /**
     * 在bitmap中判断这个skuId有没有
     * @param skuId
     * @return
     */
    boolean existSkuIdByBitMap(Long skuId);
}
