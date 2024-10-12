package com.gxcy.letaotao.web.admin.config.filter;


import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.exception.BizExceptionEnum;
import com.gxcy.letaotao.common.utils.JwtUtils;
import com.gxcy.letaotao.common.utils.LTConstants;
import com.gxcy.letaotao.web.admin.config.security.handler.LoginFailureHandler;
import com.gxcy.letaotao.web.admin.config.security.service.CustomerUserDetailService;
import com.gxcy.letaotao.web.admin.exception.CustomerAuthenticationException;
import com.gxcy.letaotao.web.admin.vo.UserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token验证过滤器
 */
@Component
public class CheckTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CustomerUserDetailService customerUserDetailService;
    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private RedisService redisService;
    //获取登录请求地址
    @Value("${request.login.url}")
    private String loginUrl;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //获取当前请求的url地址
            String url = request.getRequestURI();
            String[] paths = { // 放行路径
                    "/doc.html", loginUrl, "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/actuator/health"
            };

            for (String pattern : paths) {
                if (pathMatcher.match(pattern, url)) {
                    // 如果匹配成功，直接放行
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            //如果当前请求不是登录请求，则需要进行token验证
            this.validateToken(request);

            // 继续执行下一个过滤器
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    /**
     * 验证token
     *
     * @param request
     */
    public void validateToken(HttpServletRequest request) throws AuthenticationException {
        //从头部获取token信息
        String token = request.getHeader(LTConstants.TOKEN);
        //如果请求头部没有获取到token，则从请求的参数中进行获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter(LTConstants.TOKEN);
        }
        //如果请求参数中也不存在token信息，则抛出异常
        if (ObjectUtils.isEmpty(token)) {
            throw new CustomerAuthenticationException(BizExceptionEnum.TOKEN_NOT_EXISTS);
        }
        //判断redis中是否存在该token
        String tokenKey = CacheKeyConstants.ADMIN_TOKEN_PREFIX + token;
        String redisToken = redisService.get(tokenKey);
        //如果redis里面没有token，说明该token失效
        if (ObjectUtils.isEmpty(redisToken)) {
            throw new CustomerAuthenticationException(BizExceptionEnum.TOKEN_EXPIRED);
        }
        //如果token与Redis中的token不一致，则验证失败
        if (!token.equals(redisToken)) {
            throw new CustomerAuthenticationException(BizExceptionEnum.TOKEN_NOT_MATCH);
        }
        //如果存在token，则从token中解析出用户名
        String username = jwtUtils.getUsernameFromToken(token);
        //如果用户名为空，则解析失败
        if (ObjectUtils.isEmpty(username)) {
            throw new CustomerAuthenticationException(BizExceptionEnum.TOKEN_INVALID);
        }
        //获取用户信息
        UserVo userDetails = customerUserDetailService.loadUserByUsername(username);
        //判断用户名信息是否为空
        if (userDetails == null) {
            throw new CustomerAuthenticationException(BizExceptionEnum.TOKEN_NOT_MATCH);
        }
        //创建身份验证对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //设置到Spring Security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
