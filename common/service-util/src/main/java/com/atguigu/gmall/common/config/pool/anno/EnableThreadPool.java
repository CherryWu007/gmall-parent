package com.atguigu.gmall.common.config.pool.anno;

import com.atguigu.gmall.common.config.pool.config.AppThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.pool.anno
 * @ClassName : EnableThreadPool.java
 * @createTime : 2022/11/16 14:54
 * @Description :
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AppThreadPoolAutoConfiguration.class})
public @interface EnableThreadPool {
}
