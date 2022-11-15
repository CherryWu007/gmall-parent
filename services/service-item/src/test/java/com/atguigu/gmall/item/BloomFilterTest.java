package com.atguigu.gmall.item;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName BloomFilterTest
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-07 0:54
 * @Version 1.0
 */
public class BloomFilterTest {
    @Test
    void testBF(){
        //1、创建一个布隆过滤器
        Funnel<CharSequence> funnel = Funnels.stringFunnel(StandardCharsets.UTF_8);
        BloomFilter<CharSequence> filter = BloomFilter.create(funnel, 1000000, 0.000001);

        //2、给布隆里面添加东西
        filter.put("baidu.com");
        filter.put("jd.com");
        filter.put("google.com");

        System.out.println(filter.mightContain("atguigu.com"));
        System.out.println(filter.mightContain("atguigu.combaidu.com"));
        System.out.println(filter.mightContain("baidu.com"));
    }
}
