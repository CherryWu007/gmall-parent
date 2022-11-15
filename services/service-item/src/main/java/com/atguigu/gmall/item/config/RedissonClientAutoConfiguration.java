package com.atguigu.gmall.item.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.config
 * @ClassName : RedissonClientAutoConfiguration.java
 * @createTime : 2022/11/14 20:27
 * @Description :
 */

@Configuration
public class RedissonClientAutoConfiguration {


    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        //redis://（普通连接） or rediss://（安全连接）
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort())
                .setPassword(redisProperties.getPassword());


        RedissonClient client = Redisson.create(config);
        return client;
    }
}
