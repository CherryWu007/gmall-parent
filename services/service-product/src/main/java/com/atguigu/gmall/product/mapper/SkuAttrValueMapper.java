package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.entity.SkuAttrValue;
import com.atguigu.gmall.search.entity.SearchAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 85118
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Mapper
* @createDate 2022-11-02 09:42:19
* @Entity com.atguigu.gmall.product.entity.SkuAttrValue
*/
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {

    List<SearchAttr> getSkuAttrNameAndValue(@Param("skuId") Long skuId);
}




