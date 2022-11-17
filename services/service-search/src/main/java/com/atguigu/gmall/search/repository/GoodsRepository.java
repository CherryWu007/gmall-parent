package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.repository
 * @ClassName : GoodsRepository.java
 * @createTime : 2022/11/17 18:53
 * @Description :
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
