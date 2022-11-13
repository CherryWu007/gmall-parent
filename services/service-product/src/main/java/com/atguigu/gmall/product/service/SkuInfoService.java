package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.vo.SkuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author 85118
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfoVo vo);

    /**
     * 获取商品价格
     * @param skuId
     * @return
     */
    BigDecimal getSkuRealTimePrice(Long skuId);
}
