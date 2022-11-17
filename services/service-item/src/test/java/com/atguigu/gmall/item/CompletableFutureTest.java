package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureTest
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-09 21:56
 * @Version 1.0
 */
@SpringBootTest
public class CompletableFutureTest {
    @Autowired
    ThreadPoolExecutor executor;

    @Test
    public void all() {
        CompletableFuture<Void> info = CompletableFuture.runAsync(() -> {
            sleep3s(1000);
            System.out.println("查基本信息");
        });

        CompletableFuture<Void> price = CompletableFuture.runAsync(() -> {
            sleep3s(2000);
            System.out.println("查价格");
        });

        CompletableFuture<Void> saleattr = CompletableFuture.runAsync(() -> {
            sleep3s(3000);
            System.out.println("查销售属性");
        });

        //CompletableFuture.allOf(info,price,saleattr).get();//不调get就空转,先打印结束
        //CompletableFuture.anyOf(info,price,saleattr).get();//不调get就空转,先打印结束
        CompletableFuture.anyOf(info,price,saleattr);//不调get就空转,先打印结束

        System.out.println("结束...");
        sleep3s(10000000);

    }

    @Test
    public void exceptionTest() throws ExecutionException, InterruptedException {
        //如果远程查不到默认返回 999
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "：远程查询价格");
            return 10 + 1 / 0;
        }).exceptionally((err) -> {
            System.out.println("远程查价错误：" + err);
            return 999;
        });
        System.out.println("商品的价格："+price.get());
        sleep3s(1000000);
    }

    @Test
    public void whenTest(){
        CompletableFuture<Integer> async = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "计算....");
            return 10 + 1 / 0;
        });
        async.whenComplete((res,err) -> {
            System.out.println("res:" + res + ";err:" +err);
            if (err == null){
                System.out.println("上一个任务正常完成：结果：" + res);
            }else {
                System.out.println("上一个任务异常完成：错误："+err);
            }
        });
        sleep3s(100000);
    }

    @Test
    public void chainTaskTest(){
        //计算得到10； 对10进行叠加； 叠加的结果拼串。  拼串的结果继续处理
        CompletableFuture<Integer> async = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "计算....");
            return 6 + 7;
        });

        async.thenApply((res) -> {
            System.out.println("叠加前:" + res);
            return res * 2;
        }).thenApply((res) -> {
            System.out.println("拼串前:" + res);
            return res + ":abc";
        }).thenAccept((res) -> {
            System.out.println("最终结果:" + res);
        });

        sleep3s(10000);

    }

    @Test
    public void testThenAccept(){
        //1.计算任务
        CompletableFuture<Integer> async = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "计算...");
            return 3 + 8;
        });

        //2. 拼接上 a 得到新结果；打印
        async.thenAcceptAsync((res) -> {
            sleep3s(3000);
            System.out.println(Thread.currentThread().getName() + "==>" + res + ":A");
        },executor);

        //3、2的新结果拿来 加上当前时间；打印
        System.out.println(Thread.currentThread().getName() + "==>其他继续");

        sleep3s(1000000);
    }

    @Test
    public void testAsyncStatus() throws InterruptedException {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "哈哈:开始...");
            sleep3s(3000);
            System.out.println(Thread.currentThread().getName() + "哈哈:结束...");
        }, executor);

        //非链式,runAsync执行完毕异步执行 3/2/1
        /*runAsync.thenRun(()->{
            sleep3s(3000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿1");
        });
        runAsync.thenRun(()->{
            sleep3s(2000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿2");
        });
        runAsync.thenRun(()->{
            sleep3s(1000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿3");
        });*/

        //链式,runAsync执行完毕顺序执行1/2/3
        runAsync.thenRun(()->{
            sleep3s(3000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿1");
        }).thenRun(()->{
            sleep3s(2000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿2");
        }).thenRun(()->{
            sleep3s(1000);
            System.out.println(Thread.currentThread().getName() + "嘿嘿3");
        });

        Thread.sleep(100000);//不加这个,异步线程可能耗时久,main提前关了
    }

    private void sleep3s(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartAsync() throws ExecutionException, InterruptedException {
        System.out.println("主线程-" + Thread.currentThread().getName());

        CompletableFuture<Integer> async = CompletableFuture.supplyAsync(()->{
            System.out.println("supplyAsync:" + Thread.currentThread().getName());
            return 1;
        },executor);

        System.out.println("异步计算后的结果是：" + async.get());

        Thread.sleep(100000);//不加这个,异步线程可能耗时久,main提前关了

    }
}
