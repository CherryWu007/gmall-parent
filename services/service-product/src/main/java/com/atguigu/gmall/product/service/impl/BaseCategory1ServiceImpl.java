package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.BaseCategory1;
import com.atguigu.gmall.product.mapper.BaseCategory1Mapper;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.weball.vo.CategoryVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.service.impl
 * @ClassName : BaseCategory1ServiceImpl.java
 * @createTime : 2022/11/1 20:48
 * @Description :
 */

@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
        implements BaseCategory1Service {

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Override
    public List<CategoryVo> getCategoryTreeData() {

        return baseCategory1Mapper.getCategoryTreeData();
    }

    @Override
    public CategoryView getSkuCategoryView(Long c3Id) {
        return baseCategory1Mapper.getSkuCategoryView(c3Id);
    }
}
