package com.atguigu.gmall.common.config.pool.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.pool.properties
 * @ClassName : ThreadProperties.java
 * @createTime : 2022/11/16 14:30
 * @Description :
 */

@ConfigurationProperties(prefix = "app.thread.pool")
@Data
public class ThreadProperties {
    private int coreSize = 4;//核心大小
    private int maxSize = 8;//最大大小
    private int queueSize = 1000;//队列长度
    private long keepAliveTime = 5;//存活时间,单位为分钟
}

