package com.atguigu.gmall.common.config.pool.config;


import com.atguigu.gmall.common.config.pool.properties.ThreadProperties;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.config.pool
 * @ClassName : AppThreadPoolAutoConfiguration.java
 * @createTime : 2022/11/16 14:28
 * @Description :自定义线程池
 */
@Configuration
@EnableConfigurationProperties(ThreadProperties.class)
public class AppThreadPoolAutoConfiguration {

    @Autowired
    ThreadProperties threadProperties;

    @Value("${spring.application.name}")
    String applicationName;

    /**
     * int corePoolSize,     核心大小： cpu的核心数
     * int maximumPoolSize,  最大大小： cpu~3倍左右
     * long keepAliveTime,   保持时间： 5min； max - core = 临时线程 只要keepAliveTime时间没任务就销毁
     * TimeUnit unit,        时间单位：
     * BlockingQueue<Runnable> workQueue,  所有没被执行到的任务临时存到阻塞队列; 峰值的1.5倍
     * ThreadFactory threadFactory,        指定创建线程用的工厂
     * RejectedExecutionHandler handler    拒绝策略；
     *      CallerRunsPolicy：保证任务一定能运行。有异步线程就异步运行，没有异步线程谁提交的任务用它自己的线程同步运行
     */
    @Bean
    public ThreadPoolExecutor executor(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadProperties.getCoreSize(),
                threadProperties.getMaxSize(),
                threadProperties.getKeepAliveTime(),
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(threadProperties.getQueueSize()),
                new ThreadFactory() {
                    int i =0;
                    @Override//r:业务代码提交的异步
                    public Thread newThread(Runnable r) {
                        //自定义线程创建逻辑; 拥有什么功能？ 无限想象；
                        //监控项目中线程创建频率：  threadCount:1
                        //监控项目中运行的任务：  threadRunning： +1 -1
                        //监控结束的任务
                        //监控各种线程指标\运行时间:
                        /*
                           ()->{
                               //System.out.println("开始");
                               r.run();  //介入异步切面
                               //System.out.println("结束");
                           }
                         */
                        Thread thread=new Thread(r);
                        thread.setName("[" + applicationName + "-自定义线程池-" + (i++) + "]");
                        return thread;
                    }
                },
                new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()

        );
        return executor;
    }
}
