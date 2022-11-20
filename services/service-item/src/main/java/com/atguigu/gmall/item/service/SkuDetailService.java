package com.atguigu.gmall.item.service;

import com.atguigu.gmall.item.vo.SkuDetailVo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.service
 * @ClassName : SkuDetailService.java
 * @createTime : 2022/11/12 14:55
 * @Description :
 */

public interface SkuDetailService {
    SkuDetailVo getSkuDetail(Long skuId);

    /**
     * 更新商品热度
     * @param skuId
     */
    void upDateHotScore(Long skuId);
}
