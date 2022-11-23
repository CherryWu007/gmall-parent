package com.atguigu.gmall.weball.interceptor;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.RequestUtils;
import com.atguigu.gmall.weball.controller.CartController;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.interceptor
 * @ClassName : UserAuthFeignInterceptor.java
 * @createTime : 2022/11/21 20:56
 * @Description :feign发起远程调用的时候工作
 */
@Component
@Slf4j
public class UserAuthFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        log.info("feign拦截器接入");
        //从头到尾的所有调用都在同一个线程;除非 响应式编程、你自己通过各种方式开了线
        HttpServletRequest request= RequestUtils.getRequest();
        String tempid = request.getHeader(RedisConst.USER_TEMPID);
        String uid = request.getHeader(RedisConst.USERINFO_ID);
        //拿到网关放给weball的老请求
        template.header(RedisConst.USERINFO_ID,uid);
        template.header(RedisConst.USER_TEMPID,tempid);
    }
}
