package com.gxcy.letaotao.web.admin.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.utils.JwtUtils;
import com.gxcy.letaotao.common.utils.ResultCode;
import com.gxcy.letaotao.web.admin.vo.LoginResult;
import com.gxcy.letaotao.web.admin.vo.UserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录认证成功处理类
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisService redisService;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 设置客户端的响应内容类型
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //获取当前登录用户信息
        UserVo user = (UserVo) authentication.getPrincipal();
        // 生成token
        String token = jwtUtils.generateToken(user);
        // 设置token签名秘钥及过期时间
        long expireTime = jwtUtils.getClaimsFromToken(token).getExpiration().getTime();
        // 创建登录对象结果
        LoginResult loginResult = new LoginResult(user.getId(),
                ResultCode.SUCCESS, token, expireTime);
        // 把生成的token存到redis
        String tokenKey = CacheKeyConstants.ADMIN_TOKEN_PREFIX + token;
        redisService.set(tokenKey, token, jwtUtils.getExpiration() / 1000);

        // 使用json序列化
        String result = objectMapper.writeValueAsString(loginResult);
        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
