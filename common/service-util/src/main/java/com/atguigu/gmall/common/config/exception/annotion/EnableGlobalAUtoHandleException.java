package com.atguigu.gmall.common.config.exception.annotion;

import com.atguigu.gmall.common.config.exception.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.exception
 * @ClassName : EnableGlobalAUtoHandleException.java
 * @createTime : 2022/11/23 11:09
 * @Description :开启全局自动异常处理机制
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GlobalExceptionHandler.class})
public @interface EnableGlobalAUtoHandleException {
}
