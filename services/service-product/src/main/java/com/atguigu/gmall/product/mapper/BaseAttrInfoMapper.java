package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.product.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 85118
* @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
* @createDate 2022-11-02 09:42:19
* @Entity com.atguigu.gmall.product.entity.BaseAttrInfo
*/

public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    /**
     * 根据前端传来的id 查询这个分类下所有平台和属性值
     *
     * @param c1id
     * @param c2id
     * @param c3id
     * @return
     */
    List<BaseAttrInfo> getAttrsAndValueByCategory(@Param("c1Id") Long c1id,
                                                  @Param("c2Id") Long c2id,
                                                  @Param("c3Id") Long c3id);
}




