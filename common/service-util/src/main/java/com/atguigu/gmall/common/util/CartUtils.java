package com.atguigu.gmall.common.util;

import com.atguigu.gmall.common.constant.RedisConst;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 85118
 */
public class CartUtils {
    public static Long getUid(){
        HttpServletRequest request = GetOldRequestUtils.getRequest();
        String header = request.getHeader(RedisConst.USERINFO_ID);
        if (!StringUtils.isEmpty(header)){
            long l = Long.parseLong(header);
            return l;
        }
        return null;
    }
    public static String getTempId(){
        HttpServletRequest request = GetOldRequestUtils.getRequest();
        String tempId = request.getHeader(RedisConst.USER_TEMPID);
        return tempId;
    }
}