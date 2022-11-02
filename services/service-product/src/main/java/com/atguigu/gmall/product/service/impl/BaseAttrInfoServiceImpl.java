package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.product.entity.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.BaseAttrInfo;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 85118
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo> implements BaseAttrInfoService{

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> getAttrsAndValueByCategory(Long c1id, Long c2id, Long c3id) {

        List<BaseAttrInfo> baseAttrInfos= baseAttrInfoMapper
                .getAttrsAndValueByCategory(c1id,c2id,c3id);
        return baseAttrInfos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        if (baseAttrInfo.getId()==null) {
            //新增
            addAttrInfo(baseAttrInfo);

        }else {
            //修改
            updateAttrInfo(baseAttrInfo);
        }
    }

    private void updateAttrInfo(BaseAttrInfo baseAttrInfo) {
        //1、修改属性名信息(base_attr_info)
        baseAttrInfoMapper.updateById(baseAttrInfo);

        //那些是删除？先看数据库某一个属性原来又哪些值
        //原来有哪些值？以13号属性为例，原来有62，63，64
        //现在提交来了63，，64，说明62不要
        //delete from base_attr_value where id not in(63,64) and attr_id = 12

        //2、提取到前端提交的id集合
        ArrayList<Long> vids = new ArrayList<>();
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue attrValue : attrValueList) {
            if (attrValue.getId()!=null) {
                vids.add(attrValue.getId());
            }
        }
        //修改前端未提交的属性值信息
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",baseAttrInfo.getId());
        if (vids.size()>0){
            wrapper.notIn("id",vids);
        }
        baseAttrValueMapper.delete(wrapper);

        //3、修改属性值

        for (BaseAttrValue attrValue : attrValueList) {
            if (attrValue.getId()!=null) {
                //这些是修改
                baseAttrValueMapper.updateById(attrValue);
            }else {
                //新增
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(attrValue);
            }
        }

    }

    private void addAttrInfo(BaseAttrInfo baseAttrInfo) {
        //1、将属性名信息保存到 base_attr_info
        baseAttrInfoMapper.insert(baseAttrInfo);
        //自增id会由mybatis自动保存后填入
        Long id = baseAttrInfo.getId();
        //2、将属性值信息保存到base_attr_value
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue attrValue : attrValueList) {
            //回填属性id
            attrValue.setAttrId(id);
            baseAttrValueMapper.insert(attrValue);
        }
    }
}




