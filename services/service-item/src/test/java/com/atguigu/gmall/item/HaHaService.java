package com.atguigu.gmall.item;

/**
 * @ClassName HaHaService
 * @Description 此类描述:cglib动态代理测试类
 * @Author dangchen
 * @DateTime 2022-11-08 19:16
 * @Version 1.0
 */
public class HaHaService {
    public String haha(String name){
        System.out.println(name + "---哈哈哈哈");
        //System.out.println(name + "---哈哈哈哈" + 10/0);
        return name + ":呵呵呵";
    }
}
