package com.atguigu.gmall.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname LoginVo
 * @Date 2022/11/14 19:44
 * @Author 花非
 * @Version 1.0
 * @Description
 */
@NoArgsConstructor
@Data
public class LoginVo {
    @JsonProperty("loginName")
    private String loginName;
    @JsonProperty("passwd")
    private String passwd;
}
