package com.gxcy.letaotao.web.admin.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.exception.BizExceptionEnum;
import com.gxcy.letaotao.common.utils.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 匿名用户访问资源处理器
 */
@Component
public class AnonymousAuthenticationHandler implements AuthenticationEntryPoint {

    @Resource
    private ObjectMapper objectMapper;
    private static final Integer ERR_CODE = BizExceptionEnum.USER_NOT_LOGIN.getCode();
    private static final String ERR_MESSAGE = BizExceptionEnum.USER_NOT_LOGIN.getMsg();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 设置客户端的响应内容类型
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        // 输出错误信息
        String result = objectMapper.writeValueAsString(Result.error().code(ERR_CODE).message(ERR_MESSAGE));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
