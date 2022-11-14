package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.item.service.CacheService;
import com.atguigu.gmall.item.vo.SkuDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.service.impl
 * @ClassName : CacheServiceImpl.java
 * @createTime : 2022/11/13 19:35
 * @Description :
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public SkuDetailVo getCacheData(String cacheKey) {

        String json = redisTemplate.opsForValue().get(cacheKey);
        if (!StringUtils.isEmpty(json)){
            SkuDetailVo skuDetailVo = JSON.parseObject(json, SkuDetailVo.class);
            return skuDetailVo;
        }
        return null;
    }

    @Override
    public void saveCacheData(String cacheKey, SkuDetailVo cacheData) {
        redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(cacheData));
    }

    @Override
    public boolean existSkuIdByBitMap(Long skuId) {
        Boolean bit = redisTemplate.opsForValue().getBit(RedisConst.SKU_INFO_CACHE_KEY, skuId);
        return bit;
    }
}
