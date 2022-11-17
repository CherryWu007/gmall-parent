package com.atguigu.gmall.item;

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
 * @Description :
 */
@SpringCloudApplication
@EnableFeignClients
@EnableThreadPool
@EnableAspectJAutoProxy
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
