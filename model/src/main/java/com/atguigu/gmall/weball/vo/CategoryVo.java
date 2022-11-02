package com.atguigu.gmall.weball.vo;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.vo
 * @ClassName : CategoryVo.java
 * @createTime : 2022/11/11 14:34
 * @Description :层级嵌套结构
 */
@Data
public class CategoryVo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryVo> categoryChild;
}
