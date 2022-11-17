package com.atguigu.gmall.search.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author 85118
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "guigunews")//如果没有会自动创建
public class News {
    @Id
    private Long id;

    @Field(value = "content")
    private String content;

    @Field("publisherName")
    private String publisherName;

    @Field(value = "publishDate",type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @Field("viewCount")
    private Integer viewCount;
}
