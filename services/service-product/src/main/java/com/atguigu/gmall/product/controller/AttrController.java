package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.entity.BaseAttrInfo;
import com.atguigu.gmall.product.entity.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.fenum.qual.SwingTitlePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.controller
 * @ClassName : AttrController.java
 * @createTime : 2022/11/2 19:20
 * @Description :
 */
@RestController
@Slf4j
@RequestMapping("admin/product")
public class AttrController {

    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;
    /**
     * 查询某个平台的所有属性值
     * @param attrId
     * @return
     */
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId){
        List<BaseAttrValue> list= baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(list);
    }

    /**
     * 保存属性/修改属性
     * @RequestBody：可以将接到的接送数据转为指定的Bean对象
     */
    @PostMapping("saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        log.info("保存属性",baseAttrInfo);
        //将前端传来的数据进行保存
        baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 查询分类下平台属性
     * 请求：
     *      请求首行；GET： 请求方式  [xxxMapping]
     *              /haha：请求路径 [] @PathVariable 路径中的变化部分
     *              ?k=v:查询字符串（get的参数都在这里） @RequestParam 取出请求参数
     *      请求头；    @RequestHeader
     *      请求体：
     *            Post：表单提交： k=v&k=v @RequestParam
     *            Post：json             @RequestBody：请求体数据
     *            文件(多部件)流           @RequestPart：取出某个部件
     *                      input type="text/file"
     * @param c1id
     * @param c2id
     * @param c3id
     * @return
     */
    @GetMapping("attrInfoList/{c1id}/{c2id}/{c3id}/")
    public Result attrInfoList(@PathVariable("c1id") Long c1id,
                               @PathVariable("c2id") Long c2id,
                               @PathVariable("c3id") Long c3id){
        //数据从哪来？想办法接到前端数据
        //数据怎么处理？定义service功能，进行处理[处理逻辑甩给service]
        //数据往哪去？前端需要给后端返回一个数据
        List<BaseAttrInfo> result=  baseAttrInfoService.getAttrsAndValueByCategory(c1id,c2id,c3id);
        return Result.ok(result);
    }

}
