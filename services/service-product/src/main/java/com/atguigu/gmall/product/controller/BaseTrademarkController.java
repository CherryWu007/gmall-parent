package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.controller
 * @ClassName : BaseTrademarkController.java
 * @createTime : 2022/11/3 14:19
 * @Description :
 */
@RestController
@RequestMapping("/admin/productbaseTrademark")
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    @GetMapping("/{pn}/{ps}")
    public Result baseTrademark(@PathVariable("pn") Long pn,
                                @PathVariable("ps") Long ps){
        Page<BaseTrademark> page = new Page<>(pn ,ps);
        //分页查询
        Page<BaseTrademark> result = baseTrademarkService.page(page);
        return Result.ok(result);
    }

    /**
     * 保存品牌
     * @param baseTrademark
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        boolean save = baseTrademarkService.save(baseTrademark);
        return Result.ok(save);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable("id") Long id){
        boolean remove = baseTrademarkService.removeById(id);
        return Result.ok(remove);
    }

    /**
     * 修改品牌
     * @return
     */
    @PutMapping("/update")
    public Result update(BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);

        return Result.ok();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id){

        BaseTrademark byId = baseTrademarkService.getById(id);
        return Result.ok(byId);
    }

    /**
     * 获取所有品牌
     * @return
     */
    @GetMapping("baseTrademark/getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }
}
