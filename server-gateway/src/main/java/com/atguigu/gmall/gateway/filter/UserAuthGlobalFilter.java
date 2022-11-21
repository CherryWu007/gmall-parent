package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.ToJSON;
import com.atguigu.gmall.gateway.prperties.AuthUrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.gateway.filter
 * @ClassName : UserAuthGlobalFilter.java
 * @createTime : 2022/11/21 10:30
 * @Description :
 */
@Component
@Slf4j
public class UserAuthGlobalFilter implements GlobalFilter {

    @Autowired
    private AuthUrlProperties urlProperties;
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request= exchange.getRequest();
        //1、获取请求的当前路径
        String path=request.getURI().getPath();
        log.info("请求开始："+path);

        //2、判断【无需登录，直接放行的请求。静态资源】
        List<String> noAuthUrl = urlProperties.getNoAuthUrl();

        long count = noAuthUrl.stream()
                .filter(pattern -> antPathMatcher
                        .match(pattern, path))
                .count();
        if (count>0) {
            return chain.filter(exchange);
        }
        //3、=======内部接口无需过网关,浏览器非法访问  返回错误提醒
        long denyCount = urlProperties
                .getDenyUrl()
                .stream()
                .filter(pattern -> antPathMatcher
                        .match(pattern, path))
                .count();
        if (denyCount>0) {
            //将非法请求打回，给浏览器写一个json数据
            Result<String> result = Result.build("", ResultCodeEnum.DENY_URL);
            return responseJson(result,exchange);
        }
        //TODO 用户id透传
        //放行
        return chain.filter(exchange);
    }




    //返回响应数据
    private Mono<Void> responseJson(Result<String> result, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        //DataBuffer
        String resultJson = ToJSON.toJson(result);
        DataBuffer wrap = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        return response.writeWith(Mono.just(wrap));
    }
}
