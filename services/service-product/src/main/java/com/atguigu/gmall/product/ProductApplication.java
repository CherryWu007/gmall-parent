package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.minio.annotation.EnableMinio;
import com.atguigu.gmall.common.config.swagger.annotation.EnableAppSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product
 * @ClassName : ProductApplication.java
 * @createTime : 2022/11/1 19:44
 * @Description :SpringBoot启动默认只扫描主配置类所在的包及其子包下面的所有组件
 */
//@ComponentScan({"com.atguigu.gmall.common.config","com.atguigu.gmall.product"})
@EnableMinio
@EnableTransactionManagement
@EnableAppSwagger
@MapperScan("com.atguigu.gmall.product.mapper")
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.atguigu.gmall.feignclients.search")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
