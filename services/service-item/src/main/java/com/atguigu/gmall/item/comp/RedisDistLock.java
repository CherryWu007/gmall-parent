package com.atguigu.gmall.item.comp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.comp
 * @ClassName : RedisDistLock.java
 * @createTime : 2022/11/14 19:16
 * @Description :
 */

@Component
@Slf4j
public class RedisDistLock {
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 加锁：去redis占坑； key一样就是同一把锁。
     * @return
     */
    public String lock(){
        String token = UUID.randomUUID().toString();
        //加锁：加入自动过期时间，防止死锁
        Boolean absent = redisTemplate.opsForValue().setIfAbsent("lock", token, Duration.ofSeconds(10));
        //加失败继续加，一直到加成功即可； 阻塞式等锁；
        //自旋锁代码
        while (!absent){
            //只要没加到，疯狂重试
            //100/s
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            absent = redisTemplate.opsForValue().setIfAbsent("lock", token, Duration.ofSeconds(10));
        }
        //非常有可能刚加锁成功就断电，没来得及设置过期时间
        //加锁成功
//        redisTemplate.expire("lock", Duration.ofSeconds(10));
        return  token;
    }

    /**
     * 解锁代码
     * @param token
     */
    public void unlock(String token){
        //导致删除别人的锁；  删锁 =  得锁判断 + 删除锁
        //1、想办法别删除别人的锁？
//        if(token.equals(redisTemplate.opsForValue().get("lock"))){
//            redisTemplate.delete("lock");
//        }else {
//            log.info("别人的锁，不要删...");
//        }
        //lua脚本；原子的；
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        //返回1 就是删除成功。
        // 返回0代表删除失败：1）、别人的锁  2）、已经删除了，没有这个key
        Long lock = redisTemplate.execute(new DefaultRedisScript<>(script,Long.class), Arrays.asList("lock"),token);
        if (lock > 0){
            log.info("解锁："+token);
        }
    }
}