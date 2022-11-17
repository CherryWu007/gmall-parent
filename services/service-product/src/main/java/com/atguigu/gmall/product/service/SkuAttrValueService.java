package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.SkuAttrValue;
import com.atguigu.gmall.search.entity.SearchAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 85118
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SearchAttr> getSkuAttrNameAndValue(Long skuId);
}
