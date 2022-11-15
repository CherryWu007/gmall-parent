package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName RedissonTest
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-07 22:45
 * @Version 1.0
 */
@SpringBootTest
public class RedissonTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testClient(){
        System.out.println(redissonClient);
    }
}
