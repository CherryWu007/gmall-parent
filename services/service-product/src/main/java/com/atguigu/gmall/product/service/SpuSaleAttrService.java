package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 85118
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> getSpuSaleAttrAndValue(Long spuId, Long skuId);

    String getSkuValueJson(Long spuId);
}
