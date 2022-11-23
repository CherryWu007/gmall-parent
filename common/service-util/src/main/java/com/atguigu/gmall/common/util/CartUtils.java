package com.atguigu.gmall.common.util;

import com.atguigu.gmall.common.constant.RedisConst;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 85118
 */
public class CartUtils {
    /**
     * 获取网关透传来的用户id
     * @return
     */
    public static Long getUid(){
        HttpServletRequest request = RequestUtils.getRequest();
        String header = request.getHeader(RedisConst.USERINFO_ID);
        if (!StringUtils.isEmpty(header)){
            long l = Long.parseLong(header);
            return l;
        }
        return null;
    }

    /**
     * 获取网关透传来的临时id
     * @return
     */
    public static String getTempId(){
        HttpServletRequest request = RequestUtils.getRequest();
        String tempId = request.getHeader(RedisConst.USER_TEMPID);
        return tempId;
    }
}