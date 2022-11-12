package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.product.vo.SkuSaleAttrValueVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import feign.Param;

import java.util.List;

/**
* @author 85118
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
* @createDate 2022-11-02 09:42:19
* @Entity com.atguigu.gmall.product.entity.SpuSaleAttr
*/
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

    List<SpuSaleAttr> getSpuSaleAttrAndValueList(@Param("spuId") Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrAndValueWithOrder(@Param("spuId") Long spuId, @Param("skuId") Long skuId);

    List<SkuSaleAttrValueVo> getSkuValueJson(@Param(("spuId")) Long spuId);
}




