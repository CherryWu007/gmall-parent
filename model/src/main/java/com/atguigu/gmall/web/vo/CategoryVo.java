package com.atguigu.gmall.web.vo;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.web.vo
 * @ClassName : CateGoryVo.java
 * @createTime : 2022/11/13 14:10
 * @Description :
 */

@Data
public class CategoryVo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryVo> categoryChild;
}
