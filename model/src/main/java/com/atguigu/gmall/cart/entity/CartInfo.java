package com.atguigu.gmall.cart.entity;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车表 用户登录系统时更新冗余
 * @TableName cart_info
 * @author 85118
 */
@TableName(value ="cart_info")
@Data
public class CartInfo implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * skuid
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 放入购物车时价格
     */
    @TableField(value = "cart_price")
    private BigDecimal cartPrice;

    /**
     * 数量
     */
    @TableField(value = "sku_num")
    private Integer skuNum;


    public Integer setSkuNum(Integer skuNum){
        if (skuNum > 200 || skuNum < 0){
            throw new GmallException(ResultCodeEnum.CART_SKU_NUM);
        }
        return this.skuNum = skuNum;
    }

    /**
     * 图片文件
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * sku名称 (冗余)
     */
    @TableField(value = "sku_name")
    private String skuName;

    /**
     * 
     */
    @TableField(value = "is_checked")
    private Integer isChecked;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 实时价格
     */
    @TableField(exist = false)
    private BigDecimal skuPrice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}