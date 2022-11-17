package com.atguigu.gmall.search.vo;

import com.atguigu.gmall.search.entity.Goods;
import lombok.Data;

import java.util.List;

/**
 * @author 85118
 */
@Data
public class SearchRespVo {
    //0、当时检索时的所有条件。还需要原封不动的交给页面
    SearchParamVo searchParam;
    //品牌面包屑【当时检索的时候选中了哪个品牌，进行友好回显。】
    String trademarkParam;
    //属性面包屑
    List<AttrVo> propsParamList;
    //属性面包屑中每个对象的数据模型
    @Data
    public static class AttrVo{
        String attrName;
        String attrValue;
        Long attrId;
    }

    //1、所有商品涉及到的品牌信息 品牌列表
    List<trademarkVo> trademarkList;
    //品牌列表中每个品牌的数据

    @Data
    public static class trademarkVo{
        Long tmId;
        String tmName;
        String tmLogoUrl;
    }
    //2、所有商品涉及到的平台属性信息（属性名-所有值可能【值分布】）
    List<baseAttrInfo> attrsList;

    @Data
    public static class baseAttrInfo{
        String attrName;
        List<String> attrValueList;//属性值集合
        Long attrId;
    }
    //3、排序、分页信息
    OrderMapVo orderMap;
    Integer pageNo;//当前页
    Integer totalPages;//总页码

    @Data
    public static class OrderMapVo{
        String type;//排序类型  (1-2) 1=>综合排序 2=>价格排序
        String sort;//升降序   (asc-desc)
    }

    //当前所在的url地址信息
    String urlParam;//url参数  /list.html?category3Id=61&order=2:desc

    //4、搜索到的所有商品
    List<Goods> goodsList;

}