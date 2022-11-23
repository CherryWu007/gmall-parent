package com.atguigu.gmall.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 85118
 */
public class RequestUtils {
    /**
     * 获取原生请求
     * 利用ThreadLocal 线程绑定机制，在同一个线程的所有流程中都能共享数据
     * @return
     */
    public static HttpServletRequest getRequest(){
        //1、获取SpringMVC利用监视器给当前线程放好的请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //2、拿到原生请求
        HttpServletRequest request = attributes.getRequest();
        return request;
    }
}