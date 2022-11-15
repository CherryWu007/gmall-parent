package com.atguigu.gmall.item;

import com.atguigu.gmall.item.impl.CarserviceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName ProxyTest
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-08 19:15
 * @Version 1.0
 */
public class ProxyTest {
    @Test//cglib
    public void cglibProxy(){
        //原生方法调用
        HaHaService haHaService = new HaHaService();
        System.out.println(haHaService.haha("zhangsan"));
        System.out.println("------------"+haHaService.getClass());

        //1.创建一个增强器
        Enhancer enhancer = new Enhancer();
        //2.内部子类的方式； 没有接口就利用父类也能实现这个功能
        enhancer.setSuperclass(HaHaService.class);
        //3.设置回调
        enhancer.setCallback(new MethodInterceptor() {
            /**
             *
             * @param o 原生对象
             * @param method 当前方法
             * @param args 所有参数
             * @param methodProxy 代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                //子对象中写的拦截逻辑
                System.out.println("method开始:" + method.getName());
                //执行原父类的真正方法
                Object invokeSuper = null;
                try {
                    System.out.println("前置通知");
                    invokeSuper = methodProxy.invokeSuper(o, args);
                    System.out.println("返回通知");
                }catch (Exception e){
                    System.out.println("异常通知");
                }finally {
                    System.out.println("后置通知");
                }
                System.out.println("method执行完成");
                return invokeSuper + "哦哦哦";
            }
        });
        //4.利用增强器为某个对象创建代理对象
        HaHaService proxy = (HaHaService) enhancer.create();
        System.out.println(proxy.getClass());
        System.out.println(proxy.haha("lisi"));

    }

    @Test//proxy
    public void proxyTest(){
        //创建一个类的普通对象
        CarserviceImpl carservice = new CarserviceImpl();
        carservice.carRun("上海");
        System.out.println("原生方法调用====================");

        /**
         * ClassLoader loader, 类加载器
         * Class<?>[] interfaces, 接口
         * InvocationHandler h： 执行处理器：由这个处理器以后接管方法的执行
         */
        CarService proxy = (CarService) Proxy.newProxyInstance(carservice.getClass().getClassLoader(),
                carservice.getClass().getInterfaces(),
                new InvocationHandler() {
                    //以后 CarService 的所有方法执行，都会先过这个方法
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("加油...");
                        System.out.println("起步....");
                        //真正目标方法执行
                        args[0] += ":南窑头";
                        Object invoke = method.invoke(carservice, args);
                        System.out.println("停车入库");
                        System.out.println("==================");

                        return invoke;
                    }
                });

        //代理对象的调用方法
        proxy.carRun("西安");
        proxy.carRun("北京");
        proxy.carRun("深圳");

    }
}
