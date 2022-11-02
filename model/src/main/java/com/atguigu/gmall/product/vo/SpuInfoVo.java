package com.atguigu.gmall.product.vo;

import com.atguigu.gmall.product.entity.SpuImage;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.product.vo
 * @ClassName : SpuInfoVo.java
 * @createTime : 2022/11/10 14:29
 * @Description :spu保存数据的vo
 */
@Data
@NoArgsConstructor
public class SpuInfoVo {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("spuName")
    private String spuName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("category3Id")
    private Long category3Id;
    @JsonProperty("spuImageList")
    private List<SpuImageListDTO> spuImageList;
    @JsonProperty("spuSaleAttrList")
    private List<SpuSaleAttrListDTO> spuSaleAttrList;
    @JsonProperty("tmId")
    private Long tmId;

    @NoArgsConstructor
    @Data
    public static class SpuImageListDTO{
        @JsonProperty("imgName")
        private String imgName;
        @JsonProperty("imgUrl")
        private String imgUrl;
    }

    @Data
    @NoArgsConstructor
    public static class SpuSaleAttrListDTO{
        @JsonProperty("baseSaleAttrId")
        private Long baseSaleAttrId;
        @JsonProperty("saleAttrName")
        private String saleAttrName;
        @JsonProperty("spuSaleAttrValueList")
        private List<SpuSaleAttrValueListDTO> spuSaleAttrValueList;

        @Data
        @NoArgsConstructor
        public static class SpuSaleAttrValueListDTO{
            @JsonProperty("baseSaleAttrId")
            private Long baseSaleAttrId;
            @JsonProperty("saleAttrValueName")
            private String saleAttrValueName;
        }


    }
}
