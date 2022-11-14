package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.entity.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import feign.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 85118
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-11-02 09:42:19
* @Entity com.atguigu.gmall.product.entity.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
    /**
     * 查询商品价格
     * @param skuId
     * @return
     */
    BigDecimal BigDecimal(@Param("skuId") Long skuId);

    /**
     * 获取所有skuId
     * @return
     */
    List<Long> getSkuIds();
}




