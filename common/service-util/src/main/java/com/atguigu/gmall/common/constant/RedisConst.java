package com.atguigu.gmall.common.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.constant
 * @ClassName : RedisConst.java
 * @createTime : 2022/11/13 20:08
 * @Description :
 */

public class RedisConst {
    //skuid bitmap维护用的key
    public static final String SKUID_BITMAP_KEY ="skuids";
    public static final String SKU_INFO_CACHE_KEY = "sku:info:";
    public static final String LOCK_PREFIX = "lock:";
    public static final String CATEGORY_CACHE_KEY = "categorys";
    public static final long PAGE_SIZE = 10;
    public static final String HOT_SCORE = "hotScore";
    public static final String LOGIN_USERINFO = "key:login:user";
    public static final long LOGIN_TTL = 7L;
    public static final String TOKEN = "token";
    public static final String USER_TEMPID = "tempid";
    public static final String USERINFO_ID = "uid";
}
