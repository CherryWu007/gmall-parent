package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.service
 * @ClassName : SearchService.java
 * @createTime : 2022/11/17 15:51
 * @Description :
 */

public interface SearchService {

    /**
     * 根据检索条件检索商品并封装最终的检索结果
     * @param vo
     * @return
     */
    SearchRespVo search(SearchParamVo vo);

    void saveGoods(Goods goods);

    void deleteGoods(Long skuId);

    void updateScore(Long skuId, Long increment);
}
