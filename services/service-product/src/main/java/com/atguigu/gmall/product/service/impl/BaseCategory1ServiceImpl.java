package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.BaseCategory1;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.weball.vo.CategoryVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.service.impl
 * @ClassName : BaseCategory1ServiceImpl.java
 * @createTime : 2022/11/1 20:48
 * @Description :
 */

@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
        implements BaseCategory1Service {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    //本地缓存
    Map<String,List<CategoryVo>> cache = new ConcurrentHashMap<>();

    @Autowired
    StringRedisTemplate redisTemplate;


    public List<CategoryVo> getCategoryVoWithCache() {
        //1、先查缓存
        String categorys = redisTemplate.opsForValue().get("categorys");
        if (!StringUtils.isEmpty(categorys)){
            //2、缓存命中
            List<CategoryVo> list = JSON.parseObject(categorys, new TypeReference<List<CategoryVo>>(){});
            return list;
        }
        //3、缓存未命中
        List<CategoryVo> data = baseCategory1Mapper.getCategoryVo();
        //无论数据库有没有都要放缓存。解决缓存穿透问题
        redisTemplate.opsForValue().set("categorys",JSON.toJSONString(data));
        return data;
    }

    public List<CategoryVo> getCategoryVoLocalCache() {
        //1.先查缓存
        List<CategoryVo> categorys = cache.get("categorys");
        if (categorys != null){
            //2.缓存命中
            return categorys;
        }
        //3.缓存未命中,查询数据库(回源[回到数据库源头])
        List<CategoryVo> data = baseCategory1Mapper.getCategoryVo();
        //4.把数据放到缓存,方便下一个使用
        cache.put("categorys",data);
        return data;
    }

    @MallCache(cacheKey = RedisConst.CATEGORY_CACHE_KEY)
    @Override
    public List<CategoryVo> getCategoryVo() {
        return baseCategory1Mapper.getCategoryVo();
    }

    @Override
    public CategoryView getCategoryView(Long c3Id) {
        return baseCategory1Mapper.getCategoryView(c3Id);
    }
}
