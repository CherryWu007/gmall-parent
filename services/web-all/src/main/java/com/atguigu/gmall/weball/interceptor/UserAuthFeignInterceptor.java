package com.atguigu.gmall.weball.interceptor;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.weball.controller.CartController;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        //拿到网关放给weball的老请求
        HttpServletRequest request = CartController.threadInfo.get(Thread.currentThread());
        String tempid = request.getHeader(RedisConst.USER_TEMPID);
        String uid = request.getHeader(RedisConst.USERINFO_ID);
        template.header(RedisConst.USERINFO_ID,uid);
        template.header(RedisConst.USER_TEMPID,tempid);
    }
}
