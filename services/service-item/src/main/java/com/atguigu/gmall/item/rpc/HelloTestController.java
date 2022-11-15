package com.atguigu.gmall.item.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.comp.RedisDistLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.rpc
 * @ClassName : HelloTestController.java
 * @createTime : 2022/11/14 19:14
 * @Description :
 */


@RestController
@RequestMapping("/api/inner/rpc/item")
@Slf4j
public class HelloTestController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisDistLock redisDistLock;

    @Autowired
    RedissonClient redissonClient;

    @Value("${server.port}")
    String port;

    String token = "";

    /**
     * 测试读写锁
     * 写锁是一个排他锁（独占锁）、读锁是共享锁
     * 读读无锁
     * 写写有锁  写读有锁   读写有锁
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("read")
    public Result readData() throws InterruptedException {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("token-rwlock");
        RLock rlock = rwlock.readLock();
        rlock.lock();
        log.info("正在从远程超级慢读取token值....");
        Thread.sleep(10000);
        log.info("读取token值完成....");
        rlock.unlock();
        return Result.ok(token);
    }

    @GetMapping("write")
    public Result writeData() throws InterruptedException {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("token-rwlock");
        RLock wlock = rwlock.writeLock();
        wlock.lock();
        log.info("正在超级慢修改远程token值....");
        Thread.sleep(10000);
        token = UUID.randomUUID().toString();
        log.info("token值写完成....");
        wlock.unlock();
        return Result.ok(token);
    }

    @GetMapping("/longzhu")
    public Result longzhu() {
        RCountDownLatch downLatch = redissonClient.getCountDownLatch("longzhu7");

        downLatch.countDown();

        return Result.ok(port + "收集到一颗");

    }

    @GetMapping("/shenlong")
    public Result zhaohuanshenlong() throws InterruptedException {
        RCountDownLatch downLatch = redissonClient.getCountDownLatch("longzhu7");

        //闭锁设置数量7
        downLatch.trySetCount(7);
        downLatch.await();//等待7颗龙珠
        return Result.ok(port + "神龙来了>>>>");
    }

    //Lock lock = new ReentrantLock();//java怎么保证原子？cas

    /**
     * 不做任何操作： 1w请求 - abc=303
     * 加锁：       1w请求 - abc=10000;   200/s
     * 分布式情况下：
     * 1）、1w请求 - 加juc锁[本地锁]   abc=4400
     * 2）、1w请求 - 加分布式锁
     * 本地锁：
     * 1、可重入锁:  线程前面已经加上锁了，再要获取锁，是同一个线程就应该直接得到锁。
     * 2、公平锁：   优先让等待时间最长的人抢到锁。
     * 3、读写锁：   读读无锁、读写/写读/写写 相当于有锁
     * 4、闭锁（CountDownLatch）： 闭锁【收集七龙珠】。
     * 5、信号量：   xxx
     * 分布式锁： redisson；基于redis做好了非常多的分布式功能。锁、分布式集合
     *
     * @return
     */
    @GetMapping("/incr")
    public Result incr() {
        //lock.lock();//本地锁
        //String token = redisDistLock.lock();//自己的锁

        //获取锁
        RLock lock = redissonClient.getLock("lock");

        //阻塞式等锁=================
        //lock.lock(); //阻塞式等锁： 默认就是30s会自动续期
        //lock.lock(10, TimeUnit.SECONDS); //加上锁以后，10s过期；//明确指定锁时间，锁就不会续期

        //尝试式得锁================
//        boolean b = lock.tryLock();//尝试加锁；不等，就试一下； 默认也是30s会自动续期 //20s
//        boolean b = lock.tryLock(10, TimeUnit.SECONDS);//10s不断尝试加锁；只等有限时间；
//        boolean b = lock.tryLock(10, 15, TimeUnit.SECONDS); //10s内不断尝试加锁，加到锁以后，锁15秒释放

//        if(b){ //加锁成功
//
//        }
        lock.lock();//阻塞式等锁:默认30S就会自动续期
        try {
            //1、读取到值  0
            String abc = redisTemplate.opsForValue().get("abc");
            //2、修改值
            int i = Integer.parseInt(abc) + 1;
            //3、保存到redis
            redisTemplate.opsForValue().set("abc", i + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


        //lock.unlock();
        //redisDistLock.unlock(token);
        return Result.ok();
    }
}

