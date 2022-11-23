package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.config.exception.annotion.EnableGlobalAUtoHandleException;
import com.atguigu.gmall.common.config.pool.anno.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.cart
 * @ClassName : CartApplication.java
 * @createTime : 2022/11/21 18:03
 * @Description :
 */
@SpringCloudApplication
@EnableGlobalAUtoHandleException
@EnableThreadPool
@EnableFeignClients(basePackages = "com.atguigu.gmall.feignclients.product")
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class,args);
    }
}
