package com.atguigu.gmall.search.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author 85118
 */
@Data
public class SearchAttr {
    // 平台属性Id
    @Field(type = FieldType.Long)
    private Long attrId;
    // 平台属性值名称
    @Field(type = FieldType.Keyword)
    private String attrValue;//每个商品保存自己精确的属性名和值，后来要进行统一统计
    // 平台属性名
    @Field(type = FieldType.Keyword)
    private String attrName;


}
