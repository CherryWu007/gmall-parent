package com.atguigu.gmall.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.gateway.filter
 * @ClassName : ReactorTest.java
 * @createTime : 2022/11/20 15:44
 * @Description :
 */
//@Component
@Slf4j
public class ReactorTest implements GlobalFilter {

    /**
     *
     * 内存版消息队列：回调机制，hook机制
     *
     * 什么是响应式编程？
     * 1、响应式编程？
     *
     *      只需要少量线程;少量线程去感知处理结果：通知机制：
     *      缓冲区、通道、注册事件： 利用缓冲区+通知机制，在需要时候启动
     *      用极少得系统资源完成大量得并发操作
     *
     *         1) 万物皆可异步
     *              服务器得主线程接到数据，怎么交给业务线程进行处理
     *         2) 万物皆是流
     *              1个：mono流
     *              N个：Flux流
     *         3) 生产者与消费者模式(数据共享带一个缓冲区)：异步---【有数据就干活，没有数据就做别的事】
     *         4) 服务器稳定运行，永不宕机
     *
     * 2、命令式编程？
     *      全部由自己进行编写，程序默认串行运行
     *      aaa.aaa();
     *      ddd.vvv();
     *      遍历；
     *      tomcat这么处理请求：[每个请求来都会创建一个新线程执行]
     *      大量线程执行这些工作，浪费资源
     *          1)服务器装好tomcat，启动占用8080
     *          2)浏览器请求这个端口，tomcat收到数据，交给SpringMVC得DispatcherServlet
     *          3)SpringMVC得DispatcherServlet自己会扫描哪个方法标注了
     *                  @ xxMapping 注解处理这个请求
     *                  HandlerMapping(/a,method;  /b,method)
     *          4)利用反射执行method处理请求【HandlerAdapter】
     *          5)方法执行完成后，数据交给tomcat
     * @param args
     */
    public static void main(String[] args) {
        int age=10+1;
        //成为一个流，：具有发布订阅机制
        Mono<Integer> mono = Mono.fromCallable(()->{
            System.out.println("发布数据"+1);
            return 1;
        });
        System.out.println("开始");
        mono.subscribe(item->{
            System.out.println("张三"+item);
        });
        mono.subscribe(item->{
            System.out.println("李斯"+item);
        });
    }
    /**
     *用户id透传
     * @param exchange:封装了此次得请求响应数据
     * @param chain:   filter执行链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        log.info("请求开始"+request.getURI());
        //放行
        Mono<Void> filter = chain.filter(exchange).doFinally(item->{
            log.info("请求结束："+request.getURI());
        });

        return filter;
    }
}
