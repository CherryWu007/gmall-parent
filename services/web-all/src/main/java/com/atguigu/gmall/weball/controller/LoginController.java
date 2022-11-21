package com.atguigu.gmall.weball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.controller
 * @ClassName : LoginController.java
 * @createTime : 2022/11/20 11:42
 * @Description :
 */

@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String login(@RequestParam String originUrl, Model model){
        model.addAttribute("originUrl",originUrl);
        return "/login";
    }
}