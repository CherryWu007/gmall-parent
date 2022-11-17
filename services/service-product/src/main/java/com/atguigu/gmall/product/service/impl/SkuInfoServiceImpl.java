package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.cache.service.CacheService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.feignclients.search.SearchFeignClient;
import com.atguigu.gmall.item.vo.CategoryView;
import com.atguigu.gmall.product.entity.*;
import com.atguigu.gmall.product.service.*;
import com.atguigu.gmall.product.vo.SkuInfoVo;
import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.entity.SearchAttr;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private CacheService cacheService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SearchFeignClient searchFeignClient;
    @Autowired
    BaseTrademarkService baseTrademarkService;

    @Autowired
    BaseCategory1Service baseCategory1Service;
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

        //把skuId同步到位图
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

        //双删缓存
        cacheService.delayDoubleDel(RedisConst.SKU_INFO_CACHE_KEY);
        //删除sku的所有有关数据
        //设置此位为0，就是删除
        redisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,skuId,false);



    }

    @Override
    public void cancelSale(Long skuId) {
        //修改状态
        baseMapper.updateSkuSaleStatus(skuId,0);
        //删除es中的商品
        searchFeignClient.deleteGoods(skuId);
    }

    @Override
    public void unSale(Long skuId) {
        //修改状态
        baseMapper.updateSkuSaleStatus(skuId,1);
        //删除es中的商品 TODO
        Goods goods=prepareGoods(skuId);
        searchFeignClient.saveGoods(goods);
    }

    private Goods prepareGoods(Long skuId) {
        SkuInfo sku=getById(skuId);
        Goods goods=new Goods();
        goods.setId(skuId);
        goods.setDefaultImg(sku.getSkuDefaultImg());
        goods.setTitle(sku.getSkuName());
        goods.setPrice(sku.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        //查询品牌
        BaseTrademark trademark = baseTrademarkService.getById(sku.getTmId());
        goods.setTmId(sku.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());
        //分类信息
        CategoryView categoryView = baseCategory1Service.getCategoryView(sku.getCategory3Id());
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());
        goods.setHotScore(0L);
        //商品平台属性
        List<SearchAttr> attrs= skuAttrValueService.getSkuAttrNameAndValue(skuId);
        goods.setAttrs(attrs);


        return goods;
    }
}




