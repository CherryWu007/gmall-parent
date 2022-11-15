package com.atguigu.gmall.item;

import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.util.concurrent.*;

/**
 * @ClassName ThreadTest
 * @Description 此类描述:创建线程
 * @Author dangchen
 * @DateTime 2022-11-09 19:46
 * @Version 1.0
 */
@SpringBootTest
public class ThreadTest {

    static ExecutorService service = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("main方法开始....");

        //方法一,继承Thread，创建线程对象让线程启动
/*        HaHa haHa = new HaHa();
        haHa.start();//等CPU切到这个线程需要时间*/

        //方法二,实现runnable
/*        Hehe hehe = new Hehe();
        new Thread(hehe).start();//等CPU切到这个线程需要时间*/

        //方法三,实现callable
/*        FutureTask<Integer> futureTask = new FutureTask<>(new HeiHei());
        new Thread(futureTask).start();
        System.out.println( Thread.currentThread().getName()+ ":"+ futureTask.get());*/

        //方法四,线程池提交任务
        //1）、自己创建线程池（7大参数）【用这个】
        //2）、JUC提供的默认线程池；问题： 同时运行的任务只有四个，其他没得到运行的任务，都会存到队列中,很容易导致 OOM；
        service.execute(()->{
            System.out.println(Thread.currentThread().getName() + "轰轰轰和>>>");
        });//线程池中线程用完复用,代码不停

        System.out.println("main方法结束....");
    }
}
class HaHa extends Thread{
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+ ":"+"哈哈哈....");
    }
}

class Hehe implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+ ":"+"呵呵呵...");
    }
}

class HeiHei implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+ ":"+"嘿嘿嘿>>>>");
        return 1;
    }
}
