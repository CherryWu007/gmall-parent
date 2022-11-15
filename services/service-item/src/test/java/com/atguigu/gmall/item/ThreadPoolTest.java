package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ThreadPoolTest
 * @Description 此类描述:自定义线程池出去配置
 * @Author dangchen
 * @DateTime 2022-11-09 21:03
 * @Version 1.0
 */
@SpringBootTest
public class ThreadPoolTest {

    @Autowired
    ThreadPoolExecutor executor;

    @Test
    public void testPool(){
        System.out.println(Thread.currentThread().getName() + "testPool => 开始");
        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "哈哈哈......");
        });
        System.out.println(Thread.currentThread().getName() + "testPool => 结束");
    }
}
