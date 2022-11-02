package com.atguigu.gmall.common.config.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 专门封装配置文件中Minio的所有参数配置项
 * @author 85118
 */
//@Component
@ConfigurationProperties(prefix = "app.minio")
@Data
public class AppMinioProperties {

    //@Value(value = "endpoint")
    private String endpoint;

    //Value(value = "access-key")
    private String accessKey;

    //@Value(value = "secret-key")
    private String secretKey;

    //@Value(value = "bucket")
    private String bucket;
}
