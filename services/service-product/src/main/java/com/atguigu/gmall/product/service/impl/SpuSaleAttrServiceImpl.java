package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.product.vo.SkuSaleAttrValueVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 85118
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrAndValue(Long spuId, Long skuId) {

        return spuSaleAttrMapper.getSpuSaleAttrAndValueWithOrder(spuId,skuId);

    }

    @Override
    public String getSkuValueJson(Long spuId) {

        List<SkuSaleAttrValueVo> valueVos= spuSaleAttrMapper.getSkuValueJson(spuId);

        Map<String,String> map=new HashMap<>();
        for (SkuSaleAttrValueVo valueVo : valueVos) {
            String val = valueVo.getVal();
            Long skuId = valueVo.getSkuId();
            map.put(val,skuId.toString());
        }

        String jsonString = JSON.toJSONString(map);

        return jsonString;
    }
}




