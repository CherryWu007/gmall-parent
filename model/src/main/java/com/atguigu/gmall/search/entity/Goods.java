package com.atguigu.gmall.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author 85118
 * @Description :
 * 1、后台管理系统，点击上架。就把sku的数据保存到es中
 *  1）、（商品的全量信息）
 *  2）、（商品检索用的部分属性），如果不够可以从es中拿到符合条件的商品的id后，再去数据库查即可
 *
 *  [我们就将商品的基本信息+检索用到的所有字段都保存es即可]
 *
 * shards = 3:   3分片
 * replicas = 2: 2副本
 * 高并发有三宝：  缓存、异步、队排好
 * 高可用有三宝：  分片、复制、选领导
 */
// Index = goods , Type = info  es 7.8.0 逐渐淡化type！  修改！
@Data
@Document(indexName = "goods" , shards = 3,replicas = 2)
public class Goods {
    // 商品Id skuId
    @Id
    private Long id;

    //默认把所有的字符串 当成文本FieldType.Text； 保存的时候会分词，检索的时候分词看
    //FieldType.Keyword：这是一个不可分割的字符串, 不用为此字段建立索引index = false
    @Field(type = FieldType.Keyword, index = false)
    private String defaultImg;//为展示的冗余存储

    //  es 中能分词的字段，这个字段数据类型必须是 text！keyword 不分词！
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Double)
    private Double price;

    //  @Field(type = FieldType.Date)   6.8.1
    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // 新品

    @Field(type = FieldType.Long)
    private Long tmId;

    @Field(type = FieldType.Keyword)
    private String tmName;

    @Field(type = FieldType.Keyword)
    private String tmLogoUrl;

    @Field(type = FieldType.Long)
    private Long category1Id;

    @Field(type = FieldType.Keyword)
    private String category1Name;

    @Field(type = FieldType.Long)
    private Long category2Id;

    @Field(type = FieldType.Keyword)
    private String category2Name;

    @Field(type = FieldType.Long)
    private Long category3Id;

    @Field(type = FieldType.Keyword)
    private String category3Name;

    //  商品的热度！ 我们将商品被用户点查看的次数越多，则说明热度就越高！
    @Field(type = FieldType.Long)
    private Long hotScore = 0L;

    // 平台属性集合对象
    // Nested 支持嵌套查询; 每个商品所有的平台属性都要保存
    @Field(type = FieldType.Nested)
    private List<SearchAttr> attrs;

}