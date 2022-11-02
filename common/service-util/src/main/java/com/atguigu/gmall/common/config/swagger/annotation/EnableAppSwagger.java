package com.atguigu.gmall.common.config.swagger.annotation;

import com.atguigu.gmall.common.config.swagger.Swagger2Config;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.swagger
 * @ClassName : EnableAppSwagger.java
 * @createTime : 2022/11/10 11:20
 * @Description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
//@Import({AppMinioProperties.class, AppMinioConfiguration.class})
@Import({Swagger2Config.class})
public @interface EnableAppSwagger {
}
