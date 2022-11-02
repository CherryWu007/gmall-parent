package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.SpuInfo;
import com.atguigu.gmall.product.vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 85118
* @description 针对表【spu_info(商品表)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface SpuInfoService extends IService<SpuInfo> {
    /**
     * spu商品保存
     * @param spuInfoVo
     */
    void saveSpuInfo(SpuInfoVo spuInfoVo);
}
