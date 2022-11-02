package com.atguigu.gmall.product.service.impl;
import com.atguigu.gmall.product.entity.SpuImage;
import com.atguigu.gmall.product.entity.SpuSaleAttr;
import com.atguigu.gmall.product.entity.SpuSaleAttrValue;
import com.google.common.collect.Lists;

import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.atguigu.gmall.product.vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author 85118
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfoVo vo) {
        //1、基本信息保存到spu_info
        SpuInfo spuInfo = new SpuInfo();
        BeanUtils.copyProperties(vo,spuInfo);
        spuInfoService.save(spuInfo);

        Long spuId = spuInfo.getId();
        //2、图片信息保存spu_image
        List<SpuInfoVo.SpuImageListDTO> imageList = vo.getSpuImageList();
        ArrayList<SpuImage> images = new ArrayList<>();
        for (SpuInfoVo.SpuImageListDTO image : imageList) {
            SpuImage spuImage = new SpuImage();
            BeanUtils.copyProperties(image,spuImage);
            //回填SPUid
            spuImage.setSpuId(spuId);
            images.add(spuImage);
        }
        spuImageService.saveBatch(images);

        //3、销售属性spu_sale_attr
        //从vo中遍历出前端的所有属性
        ArrayList<SpuSaleAttr> attrs = new ArrayList<>();
        //顺便收集销售属性值
        List<SpuSaleAttrValue> attrValues =new ArrayList<>();

        List<SpuInfoVo.SpuSaleAttrListDTO> attrList = vo.getSpuSaleAttrList();
        for (SpuInfoVo.SpuSaleAttrListDTO attr : attrList) {
            //3.1 准备销售属性名信息
            SpuSaleAttr saleAttr = new SpuSaleAttr();
            BeanUtils.copyProperties(attr,saleAttr);
            //回填id
            saleAttr.setSpuId(spuId);
            attrs.add(saleAttr);

            //3.2准备销售属性值信息
            for (SpuInfoVo.SpuSaleAttrListDTO.SpuSaleAttrValueListDTO listDTO : attr.getSpuSaleAttrValueList()) {
                SpuSaleAttrValue attrValue = new SpuSaleAttrValue();
                BeanUtils.copyProperties(listDTO,attrValue);
                //回填其他参数
                attrValue.setSpuId(spuId);
                attrValue.setSaleAttrName(attr.getSaleAttrName());
                attrValues.add(attrValue);
            }

            //批量保存
            spuSaleAttrService.saveBatch(attrs);
        }

        //4、销售属性值spu_sale_attr_value

        spuSaleAttrValueService.saveBatch(attrValues);
    }
}




