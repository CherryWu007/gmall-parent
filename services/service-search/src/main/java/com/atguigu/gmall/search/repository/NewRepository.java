package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.entity.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.repository
 * @ClassName : NewRepository.java
 * @createTime : 2022/11/17 10:53
 * @Description :使用elasticsearch进行crud(增删改查)
 */

public interface NewRepository extends ElasticsearchRepository<News,Long> {
}
