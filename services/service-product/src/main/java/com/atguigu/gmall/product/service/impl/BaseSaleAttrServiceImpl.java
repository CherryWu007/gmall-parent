package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.BaseSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.mapper.BaseSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 85118
* @description 针对表【base_sale_attr(基本销售属性表)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class BaseSaleAttrServiceImpl extends ServiceImpl<BaseSaleAttrMapper, BaseSaleAttr>
    implements BaseSaleAttrService{

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrAndValueList(Long spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrAndValueList(spuId);
    }
}




