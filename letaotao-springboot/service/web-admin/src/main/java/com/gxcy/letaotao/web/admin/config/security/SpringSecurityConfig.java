package com.gxcy.letaotao.web.admin.config.security;

import com.gxcy.letaotao.web.admin.config.filter.CheckTokenFilter;
import com.gxcy.letaotao.web.admin.config.security.handler.AnonymousAuthenticationHandler;
import com.gxcy.letaotao.web.admin.config.security.handler.CustomerAccessDeniedHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * SpringSecurity配置
 */
@Configuration
//开启权限注解控制
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Resource
    private AnonymousAuthenticationHandler anonymousAuthenticationHandler;

    @Resource
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Resource
    private CheckTokenFilter checkTokenFilter;

    /**
     * 注入加密处理类
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] paths = { // 放行路径
                "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                "/doc.html", "/ws/**", "/webjars/**", "/api/sysUser/login"
        };
        // 登录前进行过滤
        http
                .cors(cors -> cors.configurationSource(request -> newCorsConfiguration())) // 允许跨域
                .csrf(AbstractHttpConfigurer::disable) // 禁用csrf
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(paths).permitAll() // 放行路径
                        .anyRequest().authenticated() // 其他路径都需要认证
                );

        // 认证失败处理
        http
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(customerAccessDeniedHandler); // 无权限处理
                    exception.authenticationEntryPoint(anonymousAuthenticationHandler); // 未登录处理
                });

        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class); // 添加token验证过滤器
        return http.build();
    }

    private CorsConfiguration newCorsConfiguration() { // 跨域配置
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configuration;
    }

}
