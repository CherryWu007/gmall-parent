package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.weball.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.rpc
 * @ClassName : CategoryRpcController.java
 * @createTime : 2022/11/11 14:41
 * @Description :提供分类数据的rpc接口
 */
@RestController
@RequestMapping("/api/inner/rpc/product/")
public class CategoryRpcController {

    @Autowired
    BaseCategory1Service baseCategory1Service;

    /**
     * 展示分类的树形结构数据
     * @return
     */
    @GetMapping("Category/tree")
    public Result getCategoryTreeData(){

        List<CategoryVo> vos=baseCategory1Service.getCategoryTreeData();

        return Result.ok(vos);
    }
}
