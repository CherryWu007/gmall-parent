package com.atguigu.gmall.user.vo;

import lombok.Data;

/**
 * @Classname LoginRespVo
 * @Date 2022/11/14 19:57
 * @Author 花非
 * @Version 1.0
 * @Description
 */
@Data
public class LoginRespVo {
    String token;
    String nickName;
    Long userId;
}
