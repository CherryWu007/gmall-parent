package com.atguigu.gmall.weball.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.weball.vo.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.feign
 * @ClassName : CategoryFeignClient.java
 * @createTime : 2022/11/11 16:07
 * @Description :
 */
//1、以后这个接口下的所有方法都是给 service-product 发送请求。
// feign会在发请求的时候自动找nacos，要到 service-product 对应的 ip:port
@FeignClient("service-product")
public interface CategoryFeignClient {

    /**
     * 获取分类树形数据
     * @return feign发完请求以后会把数据自动序列化成指定的返回值类型
     */
    @GetMapping("/api/inner/rpc/product/category/tree")
    Result<List<CategoryVo>> getCategoryTree();

}
