package com.atguigu.gmall.common.config.minio.annotation;

import com.atguigu.gmall.common.config.minio.AppMinioAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.minio.annotation
 * @ClassName : EnableMinio.java
 * @createTime : 2022/11/11 11:38
 * @Description :
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AppMinioAutoConfiguration.class})
public @interface EnableMinio {
}
