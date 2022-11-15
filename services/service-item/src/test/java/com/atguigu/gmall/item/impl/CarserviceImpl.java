package com.atguigu.gmall.item.impl;

import com.atguigu.gmall.item.CarService;

/**
 * @ClassName CarserviceImpl
 * @Description 此类描述:
 * @Author dangchen
 * @DateTime 2022-11-08 19:13
 * @Version 1.0
 */
public class CarserviceImpl implements CarService {
    @Override
    public void carRun(String dest) {
        System.out.println("汽车跑到{" + dest + "}");
    }
}
