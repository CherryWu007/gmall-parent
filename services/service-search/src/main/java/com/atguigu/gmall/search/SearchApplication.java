package com.atguigu.gmall.search;

import com.atguigu.gmall.common.config.exception.annotion.EnableGlobalAUtoHandleException;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search
 * @ClassName : SearchApplication.java
 * @createTime : 2022/11/17 9:38
 * @Description :
 */
@SpringCloudApplication
@EnableGlobalAUtoHandleException
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }
}
