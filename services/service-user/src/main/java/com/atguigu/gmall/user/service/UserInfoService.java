package com.atguigu.gmall.user.service;

import com.atguigu.gmall.user.entity.UserInfo;
import com.atguigu.gmall.user.vo.LoginRespVo;
import com.atguigu.gmall.user.vo.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 85118
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-11-20 11:32:06
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginRespVo login(LoginVo loginVo, HttpServletRequest httpServletRequest);

    void logout(String token);
}
