package com.atguigu.gmall.common.config.exception.handler;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.common.execption
 * @ClassName : GlobalExceptionHandler.java
 * @createTime : 2022/11/23 10:53
 * @Description :全局同意异常处理器
 */
@Component
//@ControllerAdvice//我是所有@Controller的切面，进行统一异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(GmallException.class)
    public Result handleGmallException(GmallException exception){
        Result<Object> fail=Result.fail();
        fail.setCode(exception.getCode());
        fail.setMessage(exception.getMessage());
        return fail;
    }

    @ExceptionHandler(Throwable.class)
    public Result handleException(Throwable throwable){
        Result<Object> fail=Result.fail();
        fail.setMessage("服务器内部错误");
        fail.setData(throwable.getStackTrace());
        return fail;
    }

}
