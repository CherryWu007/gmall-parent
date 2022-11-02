package com.atguigu.gmall.common.config.minio;

import com.atguigu.gmall.common.config.minio.properties.AppMinioProperties;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.config
 * @ClassName : AppMinioConfiguration.java
 * @createTime : 2022/11/11 11:04
 * @Description :  @EnableConfigurationProperties的作用
 *                  1、让整个属性绑定做完
 *                  2、把AppMinioProperties放到ioc容器中
 */
@Configuration
@EnableConfigurationProperties(AppMinioProperties.class)
public class AppMinioAutoConfiguration {

    @Autowired
    AppMinioProperties appMinioProperties;
    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = new MinioClient(
                appMinioProperties.getEndpoint(),
                appMinioProperties.getAccessKey(),
                appMinioProperties.getSecretKey());
        return minioClient;
    }

}
