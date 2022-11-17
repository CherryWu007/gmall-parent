package com.atguigu.gmall.search;

import com.atguigu.gmall.search.entity.News;
import com.atguigu.gmall.search.repository.NewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @ClassName EsTest
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-09 23:59
 * @Version 1.0
 */
@SpringBootTest
public class EsTest {
    @Autowired
    NewRepository newRepository;

    @Test
    void test02(){
        newRepository.deleteById(1l);
        System.out.println("删除成功...");
    }

    @Test
    void test01(){
        News news = new News(1l, "明天隔离解除", "dc", new Date(), 99);
        News news1 = new News(2l, "明天隔离解除", "lfy", new Date(), 99);
        newRepository.save(news);
        newRepository.save(news1);
        System.out.println("保存成功....");
    }
}
