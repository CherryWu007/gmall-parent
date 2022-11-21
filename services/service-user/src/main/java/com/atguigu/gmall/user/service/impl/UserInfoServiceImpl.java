package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.common.util.ToJSON;
import com.atguigu.gmall.user.vo.LoginRespVo;
import com.atguigu.gmall.user.vo.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.user.entity.UserInfo;
import com.atguigu.gmall.user.service.UserInfoService;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author 85118
* @description 针对表【user_info(用户表)】的数据库操作Service实现
* @createDate 2022-11-20 11:32:06
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginRespVo login(LoginVo loginVo, HttpServletRequest httpServletRequest) {
        //1、去查数据库有无此用户
        String loginName = loginVo.getLoginName();
        String passwd = loginVo.getPasswd();
        //加密后密码
        String password = MD5.encrypt(passwd);
        //根据账号密码查询
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", loginName);
        queryWrapper.eq("passwd", password);
        UserInfo userInfo = this.getOne(queryWrapper);
        //3、登录成功
        if (userInfo != null) {
            LoginRespVo success = new LoginRespVo();
            success.setUserId(userInfo.getId());
            success.setNickName(userInfo.getNickName());
            //生成唯一标识
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            success.setToken(token);
            //标识+用户信息
            String ipAddress = IpUtil.getIpAddress(httpServletRequest);
            userInfo.setIp(ipAddress);
            //将userinfo对象转化成json字符串
            String userInfoJson = ToJSON.toJson(userInfo);
            //将用户信息json字符串保存到redis
            redisTemplate.opsForValue().set(
                    RedisConst.LOGIN_USERINFO + token,
                    userInfoJson,
                    RedisConst.LOGIN_TTL,
                    TimeUnit.DAYS);
            return success;
        }else {
            //登录失败
            return null;
        }

    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(RedisConst.LOGIN_USERINFO + token);
    }
}




