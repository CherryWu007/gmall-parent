package com.atguigu.gmall.product.service;

import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.BaseCategory1;
import com.atguigu.gmall.weball.vo.CategoryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.service
 * @ClassName : BaseCategory1Service.java
 * @createTime : 2022/11/1 20:47
 * @Description :
 */

public interface BaseCategory1Service extends IService<BaseCategory1> {
    List<CategoryVo> getCategoryTreeData();

    CategoryView getSkuCategoryView(Long c3Id);
}