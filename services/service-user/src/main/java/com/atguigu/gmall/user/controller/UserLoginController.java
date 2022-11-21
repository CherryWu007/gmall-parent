package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.user.service.UserInfoService;
import com.atguigu.gmall.user.vo.LoginRespVo;
import com.atguigu.gmall.user.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.user.controller
 * @ClassName : UserLoginController.java
 * @createTime : 2022/11/20 11:46
 * @Description :
 */
@RequestMapping("/api/user")
@RestController
public class UserLoginController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest httpServletRequest){
        LoginRespVo vo = userInfoService.login(loginVo,httpServletRequest);
        if (vo != null) {
            return Result.ok(vo);
        }else{
            return Result.build(null, ResultCodeEnum.LOGIN_INVAILD);
        }
    }

    /**
     * 前端没有遵循规范把令牌带到 Authorization 头上
     * 1、访问页面: 令牌带到 Cookie头:token: key
     * 2、异步请求: 令牌带到 token 头
     * 退出
     * @param token
     * @return
     */
    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){
        userInfoService.logout(token);
        return Result.ok();
    }
}
