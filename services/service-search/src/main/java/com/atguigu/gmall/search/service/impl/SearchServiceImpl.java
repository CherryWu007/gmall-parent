package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.SearchService;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.service.impl
 * @ClassName : SearchServiceImpl.java
 * @createTime : 2022/11/17 15:51
 * @Description :
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    GoodsRepository goodsRepository;

    @Override
    public SearchRespVo search(SearchParamVo vo) {
        //TODO
        return null;
    }

    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }
}
