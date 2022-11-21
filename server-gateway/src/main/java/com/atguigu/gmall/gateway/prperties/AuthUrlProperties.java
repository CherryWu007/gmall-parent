package com.atguigu.gmall.gateway.prperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.gateway.prperties
 * @ClassName : AuthUrlProperties.java
 * @createTime : 2022/11/21 10:41
 * @Description :
 */
@Component
@Data
@ConfigurationProperties(prefix = "app.auth")
public class AuthUrlProperties {

    List<String> authUrl;

    List<String> denyUrl;
    //无需登录直接放行的地址
    List<String> noAuthUrl;

    String loginPage;
}
