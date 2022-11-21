package com.atguigu.gmall.weball.filter;


import javax.servlet.*;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.weball.filter
 * @ClassName : HelloFilter.java
 * @createTime : 2022/11/20 16:39
 * @Description :
 */

public class HelloFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws
            IOException, ServletException {

    }
}
