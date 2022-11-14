package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.init
 * @ClassName : InitTask.java
 * @createTime : 2022/11/13 20:02
 * @Description :启动的初始化任务
 * 1、使用SpringBoot的启动监听机制
 * 2、使用组件的初始化机制
 */
@Slf4j
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class InitTask {

    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    StringRedisTemplate redisTemplate;


    @PostConstruct //组件对象创建好以后干什么
    public void initSkuIdBitMap(){
        log.info("init task对象完全创建好以后执行... 正在准备bitmap数据");
        //1、查询出所有的skuid。 2亿 * 8byte = 1600 gbyte = 1.6G。应用就会oom
        //使用分页查询，查出所有的数据
        List<Long> ids =  skuInfoService.getSkuIds();

        //2、初始化bitmap
        for (Long id:ids){
            //都去占位。魔法值:抽取常量
            redisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,id,true);
        }
        log.info("skuids 的bitmap 初始化成功");
    }
}
