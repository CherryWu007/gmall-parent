package com.atguigu.gmall.product.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.vo
 * @ClassName : SkuInfoVo.java
 * @createTime : 2022/11/10 16:40
 * @Description :
 */
@Data
@NoArgsConstructor
public class SkuInfoVo {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("spuId")
    private Long spuId;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("skuName")
    private String skuName;
    @JsonProperty("weight")
    private BigDecimal weight;
    @JsonProperty("skuDesc")
    private String skuDesc;
    @JsonProperty("category3Id")
    private Long category3Id;
    @JsonProperty("tmId")
    private Long tmId;
//以上是sku表的基本信息
    @JsonProperty("skuAttrValueList")
    private List<SkuAttrValueListDTO> skuAttrValueList;
    @JsonProperty("skuSaleAttrValueList")
    private List<SkuSaleAttrValueListDTO> skuSaleAttrValueList;
    @JsonProperty("skuInageList")
    private List<SkuImageListDTO> skuImageList;
    @JsonProperty("skuDefaultImg")
    private Long skuDefaultImg;

    @NoArgsConstructor
    @Data
    public static class SkuAttrValueListDTO{
        @JsonProperty("attrId")
        private Long attrId;
        @JsonProperty("valueId")
        private Long valveId;
    }

    @NoArgsConstructor
    @Data
    public static class SkuSaleAttrValueListDTO {

        @JsonProperty("saleAttrValLueId")
        private Long saleAttrValueId;
        @JsonProperty("saleAttrValueName")
        private String saleAttrValueName;
        @JsonProperty("baseSaleAttrId")
        private Long baseSaleAttrId;
        @JsonProperty("saleAttrName")
        private String saleAttrName;
    }

    @Data
    @NoArgsConstructor
    public static class SkuImageListDTO{
        @JsonProperty("spuImgId")
        private Long spuImgId;
        @JsonProperty("imgName")
        private String imgName;
        @JsonProperty("imgUrl")
        private String imgUrl;
        @JsonProperty("isDefault")
        private String isDefault;
    }







}
