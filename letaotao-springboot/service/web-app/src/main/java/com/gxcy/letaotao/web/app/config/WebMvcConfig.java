package com.gxcy.letaotao.web.app.config;

import com.gxcy.letaotao.common.config.StringToBaseEnumConverterFactory;
import com.gxcy.letaotao.web.app.config.interceptor.AuthenticationInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;
    @Resource
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) { // 注册枚举转换器
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] paths = { // 放行路径
                "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                "/doc.html", "/ws/**", "/webjars/**", "/api/wx_user/login",
                "/api/wx_categories/tree",
                "/api/wx_posts/list/**", "/api/wx_posts/*",
                "/api/wx_comments/list",
                "/api/wx_products/*", "/api/wx_products/list/**",
                "/api/wx_user/list/**", "/api/wx_user/*", "/api/wx_user/logout",
                "/api/wx_evaluates/list/user/*",
                "/api/wx_orders/*"
        };
        registry.addInterceptor(this.authenticationInterceptor).addPathPatterns("/api/**").excludePathPatterns(paths);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
