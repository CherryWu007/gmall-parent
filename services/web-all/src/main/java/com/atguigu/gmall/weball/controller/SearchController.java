package com.atguigu.gmall.weball.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;
import com.atguigu.gmall.feignclients.search.SearchFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.controller
 * @ClassName : SearchController.java
 * @createTime : 2022/11/17 14:36
 * @Description :
 */
@Controller
public class SearchController {

    @Autowired
    SearchFeignClient searchFeignClient;

    /**
     * 前端检索会传递 来的所有参数
     * 1.检索
     * 关键字: keyword=小米
     * 分类: category3Id=61 category2Id=3 category1Id=2
     * 品牌: trademark=1:小米  值的格式: 品牌id:品牌名
     * 属性: props=23:8G:运行内存&props=24:128G:机身内存  值的格式: 属性id: 属性值: 属性名
     * 2.排序, 分页
     * 排序: order=1:asc 值格式:排序规则: 排序方式 1=综合排序 2=价格排序
     * 分页: pageNo=1
     * @return
     */

    @GetMapping("/list.html")
    public String searchList(SearchParamVo searchParamVo, Model model){
        //TODO
        //接收前端传递过来的所以普搜索条件
        //2、远程调用search进行搜索
        Result<SearchRespVo> result = searchFeignClient.searchGoods(searchParamVo);
        SearchRespVo respVo=result.getData();
        //3、拿到所有检索结果进行渲染
        model.addAttribute("searchParam",respVo.getSearchParam());
        model.addAttribute("trademarkParam",respVo.getTrademarkParam());
        //品牌面包屑
        model.addAttribute("propsParamList",respVo.getPropsParamList());
        //品牌列表
        model.addAttribute("trademarkList",respVo.getTrademarkList());
        //属性列表
        model.addAttribute("attrsList",respVo.getAttrsList());

        model.addAttribute("orderMap",respVo.getOrderMap());
        model.addAttribute("pageNo",respVo.getPageNo());
        model.addAttribute("totalPages",respVo.getTotalPages());
        model.addAttribute("urlParam",respVo.getUrlParam());
        model.addAttribute("goodsList",respVo.getGoodsList());


        return "list/index";
    }
}
