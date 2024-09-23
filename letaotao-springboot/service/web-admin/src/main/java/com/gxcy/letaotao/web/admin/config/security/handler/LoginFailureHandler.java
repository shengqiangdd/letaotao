package com.gxcy.letaotao.web.admin.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.exception.CustomerAuthenticationException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 用户认证失败处理类
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Resource
    private ObjectMapper objectMapper;
    private static final Integer ERR_CODE = 500;
    private static final String ERR_MESSAGE = "登录失败！";
    private static final String ACCOUNT_EXPIRED = "账户过期，登录失败！";
    private static final String ACCOUNT_LOCKED = "账户被锁，登录失败！";
    private static final String ACCOUNT_DISABLED = "账户被禁用，登录失败！";
    private static final String INVALID_CREDENTIALS = "用户名或密码错误，登录失败！";
    private static final String USERNAME_NOT_FOUND = "账户不存在，登录失败！";


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 设置客户端的响应内容类型
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        String message = null; // 提示信息
        int code = ERR_CODE; // 错误编码
        // 判断异常类型
        if (exception instanceof AccountExpiredException) { //账户过期
            message = ACCOUNT_EXPIRED;
        } else if (exception instanceof BadCredentialsException) { //密码错误
            message = INVALID_CREDENTIALS;
        } else if (exception instanceof DisabledException) { //账户被禁用
            message = ACCOUNT_DISABLED;
        } else if (exception instanceof LockedException) { //账户被锁
            message = ACCOUNT_LOCKED;
        } else if (exception instanceof InternalAuthenticationServiceException) { //账户不存在
            message = USERNAME_NOT_FOUND;
        } else if (exception instanceof CustomerAuthenticationException) { //自定义异常
            message = exception.getMessage();
            int errorCode = ((CustomerAuthenticationException) exception).getErrorCode();
            if (errorCode > 0) {
                code = errorCode;
            } else {
                code = HttpServletResponse.SC_UNAUTHORIZED;
            }
        } else {
            message = ERR_MESSAGE;
        }
        // 将错误信息转换成JSON
        String result = objectMapper.writeValueAsString(Result.error().code(code).message(message));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
