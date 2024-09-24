package com.gxcy.letaotao.web.app.config.interceptor;

import com.gxcy.letaotao.common.utils.JwtUtils;
import com.gxcy.letaotao.common.utils.LTConstants;
import com.gxcy.letaotao.web.app.utils.LoginUser;
import com.gxcy.letaotao.web.app.utils.LoginUserHolder;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtils jwtUtils;

    private static final String[] PATH_PATTERNS = {
            "/api/wx_posts/\\d+", "/api/wx_products/\\d+",
            "/api/wx_user/\\d+", "/api/wx_orders/\\d+"
    };

    private static final String[] PATH_PATTERNS1 = {
            "/api/wx_products/list/\\w+/page",
            "/api/wx_posts/list/\\w+/page"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 如果是 GET 请求并且路径包含 /{id}，则直接返回 true 表示不拦截
        for (String path : PATH_PATTERNS) {
            if (HttpMethod.GET.name().equals(method) && requestURI.matches(path)) {
                return true;
            }
        }

        String token = request.getHeader(LTConstants.TOKEN);

        // 特殊处理：查询当前用户的商品或帖子
        for (String path : PATH_PATTERNS1) {
            if (HttpMethod.GET.name().equals(method) && requestURI.matches(path)) {
                if (requestURI.contains("cur_user")) { // 获取当前用户的商品、帖子列表都要登录
                    String currentUserId = request.getParameter("currentUserId");
                    if (StringUtils.isEmpty(token) || ObjectUtils.isEmpty(currentUserId)) {
                        break;
                    }
                } else {
                    return true;
                }
            }
        }

        Claims claims = jwtUtils.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        LoginUserHolder.setLoginUser(new LoginUser(userId, username));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }

}