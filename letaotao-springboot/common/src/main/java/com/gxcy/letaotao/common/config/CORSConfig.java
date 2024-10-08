package com.gxcy.letaotao.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"). // 允许所有的访问请求（访问路径）
                allowedMethods("*"). // 允许所有的请求方法访问该跨域资源服务器
                allowedOriginPatterns("*"). // 允许所有的域访问该跨域资源服务器
                allowedHeaders("*"). // 允许所有的请求header访问
                allowCredentials(true);
    }
}
