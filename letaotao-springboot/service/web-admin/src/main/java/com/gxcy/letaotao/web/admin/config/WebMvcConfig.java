package com.gxcy.letaotao.web.admin.config;

import com.gxcy.letaotao.common.config.StringToBaseEnumConverterFactory;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) { // 注册枚举转换器
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }

}
