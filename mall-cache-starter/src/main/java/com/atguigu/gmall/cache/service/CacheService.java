package com.atguigu.gmall.cache.service;


import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.TimeUnit;

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


    /**
     * 给缓存中保存数据并指定过期时间
     * @param cacheKey
     * @param retVal
     * @param ttl
     * @param unit
     */
    void saveCacheData(String cacheKey, Object retVal, long ttl, TimeUnit unit);


    /**
     * 把缓存中的数据转成指定的简单类型
     * @param cacheKey
     * @param t
     * @param <T>
     * @return
     */
    <T>T getCacheDataObj(String cacheKey,Class<T> t);

    /**
     * 判定指定bitmap中是否有某个索引位置
     * @param bmKey
     * @param parseLong
     * @return
     */
    boolean existFromBitMap(String bmKey, long parseLong);

    /**
     * 把缓存中的数据转成指定的复杂类型
     * @param cacheKey
     * @param typeReference
     * @return
     */
    Object getCacheDataObj(String cacheKey, TypeReference typeReference);//jackson：不会出现错误结果

    /**
     * 延迟双删
     * @param key
     */
    void delayDoubleDel(String key);
}
