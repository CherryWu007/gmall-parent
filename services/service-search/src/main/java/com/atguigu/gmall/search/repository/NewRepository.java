package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.entity.News;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.repository
 * @ClassName : NewRepository.java
 * @createTime : 2022/11/17 10:53
 * @Description :使用elasticsearch进行crud(增删改查)
 *      ES中做一些自定义查询要写 DSL
 *      1、ElasticsearchRestTemplate 可以对es crud： 完全自定义
 *      2、自己写的继承于ElasticsearchRepository的Repository： 覆盖90%场景
 *
 *      SpringData允许只做方法命名就能crud
 */
@Repository//这是一个操作dao层；
public interface NewRepository extends ElasticsearchRepository<News,Long> {
    //方法起名https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#populator.namespace-dao-config

    //查出所有发布者是cherrywu的新闻
    List<News> findAllByPublisherNameEquals(String publisherName);

    // find/getAllBy字段条件
    //查询所有浏览量大于5，并且发布时间是三天前的所有新闻
    List<News> findAllByViewCountGreaterThanAndPublishDateBefore(Integer viewCount, Date publishDate);

    //仅限于单索引的简单查询
    Long countAllByIdGreaterThan(Long id);

    //jpa：Java Persistent API ： Java持久化API
    @Query(
            "{\n" +
                    "    \"match\": {\n" +
                    "      \"publisherName\": \"?0\"\n" +
                    "    }\n" +
                    "  }"
    )
    List<News> getYiDui(String name);

}
