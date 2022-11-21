package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;

import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.ToJSON;
import com.atguigu.gmall.gateway.prperties.AuthUrlProperties;
import com.atguigu.gmall.user.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
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

    /**
     * 用户id透传
     * 1、静态资源直接放行
     * 2、内部接口直接拒绝
     * 3、必须登录才能访问
     * @param exchange
     * @param chain
     * @return
     */

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
        //4、必须登录才能访问的请求，需要验证token【判断】

        long authCount = urlProperties.getAuthUrl().stream().filter(pattern -> antPathMatcher.match(pattern, path)).count();
        if (authCount>0) {
            //验证是否登录，没登录直接打回
            //1）拿到token数据
            String userToken=getUserHeaderInfo(exchange,RedisConst.TOKEN);
            //判定token在redis中有没有
            boolean b=existLoginUser(userToken);
            //2)判断是否有token
            if (StringUtils.isEmpty(userToken)||!b) {
                //直接打回登录页
                return redirectPage(exchange,urlProperties.getLoginPage());
            }
        }


        //TODO 用户id透传;  如果有用户id就透传
        String token = getUserHeaderInfo(exchange, RedisConst.TOKEN);
        //获取用户的登陆信息
        UserInfo userInfo = getLoginInfo(token);
        //普通请求不带  没带不用管
        //登陆请求   验证合法性    带了必须验证
        if (!StringUtils.isEmpty(token)) {
            //是否存在key
            boolean b = existLoginUser(token);
            //不存在的话  直接重定向到登陆页面
            if (!b){
                return redirectPage(exchange,urlProperties.getLoginPage());
            }
            //存在的话验证  存储的ip是否一致
            if (b){
                String ip = userInfo.getIp();
                String gatwayIpAddress = IpUtil.getGatwayIpAddress(request);
                //ip不相等直接重定向到登陆页面  可能 用户令牌被调用
                if (!gatwayIpAddress.equals(ip)){
                    return redirectPage(exchange,urlProperties.getLoginPage());
                }
            }
        }
        //放行
        //网关：
        //1)谁来都行
        //2)谁来都不行
        //3)自定义规则： 各种规则 需要登录的验证登录
        //4)剩下的都是普通请求，直接放


        //return chain.filter(exchange);
        //用户id和临时id有哪个透传哪个，都有就都传
        return tempIdAndUserInfoIdPassThrough(exchange,chain,userInfo);
    }
    private Mono<Void> tempIdAndUserInfoIdPassThrough(ServerWebExchange exchange,
                                                      GatewayFilterChain chain,
                                                      UserInfo userInfo) {
        //获取临时id的token
        String tempId = getUserHeaderInfo(exchange, "userTempId");
        //修改exchange中的请求信息  在透传
        //新建改变请求信息的  建造者
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        //判断临时id  是否为空
        if (!StringUtils.isEmpty(tempId)) {
            //有数据直接构建到请求信息中
            builder.header(RedisConst.USER_TEMPID,tempId);
        }
        //判断userinfo  id是否为空
        if (userInfo != null){
            builder.header(RedisConst.USERINFO_ID,userInfo.getId().toString());
        }
        //构建新的servlet内的请求和响应
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder.build())
                .response(exchange.getResponse())
                .build();



        return chain.filter(newExchange);
    }

    private UserInfo getLoginInfo(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String userInfoJson = redisTemplate.opsForValue().get(RedisConst.LOGIN_USERINFO + token);
        if(StringUtils.isEmpty(userInfoJson)){
            return null;
        }
        return ToJSON.toObject(userInfoJson, UserInfo.class);
    }

    private boolean existLoginUser(String userToken) {
        String key = RedisConst.LOGIN_USERINFO + userToken;
        Boolean haskey = redisTemplate.hasKey(key);
        return haskey;
    }

    /**
     * 重定向到指定页面
     * @param exchange 请求响应
     * @param loginPage 浏览器需要自行重定向
     * @return
     */
    private Mono<Void> redirectPage(ServerWebExchange exchange, String loginPage) {
        ServerHttpResponse response = exchange.getResponse();
        String path= "";
        try {
            path = exchange.getRequest().getURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //1、重定向：HTTP   302状态码+location响应头
        response.setStatusCode(HttpStatus.FOUND);
        String url=loginPage+"?originUrl="+path;
        response.getHeaders().add("Location",url);
        //假token无限重定向  清除响应数据的cookie   并赋值新的
        ResponseCookie tokenCookie = ResponseCookie.from("token", "")
                .maxAge(0)
                .path("/")
                .domain(".gmall.com")
                .build();
        ResponseCookie infoCookie = ResponseCookie.from("userInfo", "")
                .maxAge(0)
                .path("/")
                .domain(".gmall.com")
                .build();
        response.addCookie(tokenCookie);
        response.addCookie(infoCookie);
        return response.setComplete();
    }

    /**
     * 获取用户令牌
     * @param exchange
     * @return
     */
    private String getUserHeaderInfo(ServerWebExchange exchange,String key) {
        ServerHttpRequest request=exchange.getRequest();
        //先看cookie中有没有token
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        if (cookies!=null) {
            HttpCookie token = cookies.getFirst(key);
            if (token!=null) {
                String value = token.getValue();
                if (StringUtils.isEmpty(value)){
                    return value;
                }
            }

        }
        //2、cookie没有，看token中有没有
        String token = request.getHeaders().getFirst(key);
        return token;
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
