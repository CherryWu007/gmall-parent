package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.BaseSaleAttr;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 85118
* @description 针对表【base_sale_attr(基本销售属性表)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface BaseSaleAttrService extends IService<BaseSaleAttr> {
    /**
     * 查询spu对应的销售属性名和值列表
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrAndValueList(Long spuId);
}
