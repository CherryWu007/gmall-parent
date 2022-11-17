package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.search.entity.SearchAttr;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.SkuAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.mapper.SkuAttrValueMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 85118
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService{

    @Override
    public List<SearchAttr> getSkuAttrNameAndValue(Long skuId) {
        return baseMapper.getSkuAttrNameAndValue(skuId);
    }
}




