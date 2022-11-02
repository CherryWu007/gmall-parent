package com.atguigu.gmall.product.service;

import com.atguigu.gmall.product.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 85118
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-11-02 09:42:19
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {
    /**
     * 根据前端传来的id 查询这个分类下所有平台和属性值
     * @param c1id
     * @param c2id
     * @param c3id
     * @return
     */
    List<BaseAttrInfo> getAttrsAndValueByCategory(Long c1id, Long c2id, Long c3id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
}
