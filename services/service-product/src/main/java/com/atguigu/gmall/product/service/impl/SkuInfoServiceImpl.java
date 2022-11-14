package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.product.entity.SkuAttrValue;
import com.atguigu.gmall.product.entity.SkuImage;
import com.atguigu.gmall.product.entity.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.atguigu.gmall.product.vo.SkuInfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.entity.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @author 85118
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-11-02 09:42:19
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    StringRedisTemplate redisTemplate;
    @Override
    @Transactional
    public void saveSkuInfo(SkuInfoVo vo) {
        //1、sku的基本信息保存完成
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(vo,skuInfo);
        save(skuInfo);

        Long skuId =skuInfo.getId();

        //2、sku图片信息，保存到sku_image
        ArrayList<SkuImage> images = new ArrayList<>();
        List<SkuInfoVo.SkuImageListDTO> imageList = vo.getSkuImageList();
        for (SkuInfoVo.SkuImageListDTO image : imageList) {
            SkuImage skuImage = new SkuImage();
            BeanUtils.copyProperties(image,skuImage);
            skuImage.setSkuId(skuId);
            images.add(skuImage);
        }

        skuImageService.saveBatch(images);

        //3、存储平台属性名和值信息，保存到sku_attr_value
        ArrayList<SkuAttrValue> attrValues = new ArrayList<>();
        List<SkuInfoVo.SkuAttrValueListDTO> attrValueList = vo.getSkuAttrValueList();
        for (SkuInfoVo.SkuAttrValueListDTO attr : attrValueList) {
            SkuAttrValue attrValue =new SkuAttrValue();
            BeanUtils.copyProperties(attr,attrValue);
            attrValue.setSkuId(skuId);

            attrValues.add(attrValue);
        }

        skuAttrValueService.saveBatch(attrValues);

        //4、sku的销售属性名和值保存到sku_sale_attr_value
        List<SkuSaleAttrValue> values=new ArrayList<>();
        List<SkuInfoVo.SkuSaleAttrValueListDTO> skuSaleAttrValueList = vo.getSkuSaleAttrValueList();
        for (SkuInfoVo.SkuSaleAttrValueListDTO saleAttr : skuSaleAttrValueList) {
            SkuSaleAttrValue skuSaleAttrValue= new SkuSaleAttrValue();
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(vo.getSpuId());
            skuSaleAttrValue.setSaleAttrValueId(saleAttr.getSaleAttrValueId());
            values.add(skuSaleAttrValue);
        }
        skuSaleAttrValueService.saveBatch(values);

        //TODO 把skuId同步到位图
        redisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,skuId,true);

    }

    @Override
    public BigDecimal getSkuRealTimePrice(Long skuId) {
        //select price from sku_info where id=skuId

        return baseMapper.BigDecimal(skuId);
    }

    @Override
    public List<Long> getSkuIds() {
        return baseMapper.getSkuIds();


    }

    @Override
    public void removeSku(Long skuId) {
        removeById(skuId);
        //TODO 删除sku的所有有关数据

        //设置此位为0，就是删除
        redisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,skuId,false);
    }
}




