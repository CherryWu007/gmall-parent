package com.atguigu.gmall.search.vo;

import lombok.Data;

/**
 * @author 85118
 */
@Data
public class SearchParamVo {
    String keyword;
    Long category3Id;
    Long category2Id;
    Long category1Id;
    String trademark;
    String[] props;
    String order;
    Integer pageNo;
}