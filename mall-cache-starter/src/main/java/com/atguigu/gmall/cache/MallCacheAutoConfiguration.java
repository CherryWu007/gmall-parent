package com.atguigu.gmall.cache;

import com.atguigu.gmall.cache.aspect.CacheAspect;
import com.atguigu.gmall.cache.service.impl.CacheServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cache
 * @ClassName : MallCacheAutoConfiguration.java
 * @createTime : 2022/11/15 20:46
 * @Description :
 */
@Import({
        CacheAspect.class,
        CacheServiceImpl.class
})
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class MallCacheAutoConfiguration {
}
