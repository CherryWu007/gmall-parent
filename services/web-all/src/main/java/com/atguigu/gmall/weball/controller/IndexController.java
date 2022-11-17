package com.atguigu.gmall.weball.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feignclients.product.CategoryFeignClient;
import com.atguigu.gmall.weball.vo.CategoryVo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.controller
 * @ClassName : IndexController.java
 * @createTime : 2022/11/11 14:21
 * @Description :
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryFeignClient feignClient;

    @GetMapping("/")
    public String indexpage(Model model){
        Result<List<CategoryVo>> categoryTree = feignClient.getCategoryTree();
        //远程调用
        List<CategoryVo> vos=categoryTree.getData();
        model.addAttribute("list",vos);
        return "index/index";
    }
}
