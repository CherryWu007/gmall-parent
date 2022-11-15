package com.atguigu.gmall.cache.service.impl;


import com.atguigu.gmall.cache.service.CacheService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    ObjectMapper objectMapper=new ObjectMapper();

    ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(10);


    @Override
    public <T> T getCacheDataObj(String cacheKey, Class<T> t) {
        String json = redisTemplate.opsForValue().get(cacheKey);
        if (!StringUtils.isEmpty(json)) {
            T t1 = null;
            try {
                t1 = objectMapper.readValue(json, t);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return t1;
        }

        return null;
    }

    @Override
    public void saveCacheData(String cacheKey, Object retVal, long ttl, TimeUnit unit) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(retVal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (ttl > 0){
            redisTemplate.opsForValue().set(cacheKey,json,ttl,unit);
        }else {
            redisTemplate.opsForValue().set(cacheKey,json);
        }
    }
    @Override
    public boolean existFromBitMap(String bmKey, long parseLong) {
        Boolean b = redisTemplate.opsForValue().getBit(bmKey, parseLong);
        return b;
    }

    @Override
    public Object getCacheDataObj(String cacheKey, TypeReference typeReference) {
        String json = redisTemplate.opsForValue().get(cacheKey);
        if (!StringUtils.isEmpty(json)) {
            Object readValue = null;
            try {
                readValue = objectMapper.readValue(json, typeReference);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return readValue;
        }
        return null;
    }

    @Override
    public void delayDoubleDel(String key) {
        redisTemplate.delete(key);
        scheduledPool.schedule(() -> {
            redisTemplate.delete(key);
        },3,TimeUnit.SECONDS);
    }
}
