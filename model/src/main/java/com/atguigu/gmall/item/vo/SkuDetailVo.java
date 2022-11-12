package com.atguigu.gmall.item.vo;

import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.vo
 * @ClassName : SkuDetailVo.java
 * @createTime : 2022/11/12 13:57
 * @Description :
 */
@Data
public class SkuDetailVo {

    //分类(页面导航位置的分类信息)
    private CategoryView categoryView;

    //sku信息(基本信息,图片)
    private SkuInfo skuInfo;

    //spu小说属性列表(页面销售属性组合部分的数据)
    private List<SpuSaleAttr> spuSaleAttrList;

    //每种sku组合怎么跳转
    private String valuesSkuJson;

    //商品价格
    private BigDecimal price;
}
