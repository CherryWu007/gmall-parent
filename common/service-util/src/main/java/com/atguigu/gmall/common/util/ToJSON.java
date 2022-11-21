package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Classname ToJSON
 * @Date 2022/11/14 20:15
 * @Author 花非
 * @Version 1.0
 * @Description
 */

public class ToJSON {

    public static String toJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T toObject(String json,Class<T> t) {
        ObjectMapper objectMapper = new ObjectMapper();
        T t1 = null;
        try {
            t1 = objectMapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t1;
    }
}
