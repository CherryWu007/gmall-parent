package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.BaseCategory1;
import com.atguigu.gmall.weball.vo.CategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.mapper
 * @ClassName : BaseCategory1Mapper.java
 * @createTime : 2022/11/1 20:47
 * @Description :
 */

@Mapper //让SpringBoot启动扫描进去
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {

    List<CategoryVo> getCategoryTreeData();

    CategoryView getSkuCategoryView(@Param("c3Id") Long c3Id);
}
