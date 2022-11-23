package com.atguigu.gmall.item;

import com.atguigu.gmall.common.config.exception.annotion.EnableGlobalAUtoHandleException;
import com.atguigu.gmall.common.config.pool.anno.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item
 * @ClassName : ItemApplication.java
 * @createTime : 2022/11/12 14:10
 * @Description :@EnableFeignClients:开启远程调用，只扫描主类所在的包和子包
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feignclients.product","com.atguigu.gmall.feignclients.search"})
@EnableThreadPool
@EnableAspectJAutoProxy
@EnableGlobalAUtoHandleException
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
