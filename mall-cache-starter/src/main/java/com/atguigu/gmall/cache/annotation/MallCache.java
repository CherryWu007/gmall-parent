package com.atguigu.gmall.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.annotation
 * @ClassName : MallCache.java
 * @createTime : 2022/11/15 14:12
 * @Description :
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MallCache {
    /**
     * 缓存使用的key; 支持编写SpEL； 动态计算的部分用 #{} 包裹起来
     * @return
     */
    String cacheKey() default "";

    /**
     * 如果需要bitmap判断，则指定bitmap的key
     * @return
     */
    String bitmapKey() default "";

    /**
     * 如果需要bitmap判断，则指定索引；支持表达式
     * @return
     */
    String bitmapIndex() default "";

    /**
     * 数据的过期时间
     * @return
     */
    long ttl() default 0L;

    /**
     * 时间单位
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}

