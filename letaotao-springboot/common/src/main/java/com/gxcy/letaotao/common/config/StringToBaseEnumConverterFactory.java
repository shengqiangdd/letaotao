package com.gxcy.letaotao.common.config;

import com.gxcy.letaotao.common.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * 字符串转枚举
 */
@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {

                for (T enumConstant : targetType.getEnumConstants()) { // 枚举类中定义的所有枚举值
                    if (enumConstant.getCode() instanceof Integer) { // 判断是否是数字
                        if (enumConstant.getCode().equals(Integer.valueOf(source))) {
                            return enumConstant;
                        }
                    } else if (enumConstant.getCode() instanceof String) { // 判断是否是字符串
                        if (enumConstant.getCode().equals(source)) {
                            return enumConstant;
                        }
                    }
                }
                throw new IllegalArgumentException("非法的枚举值:" + source);
            }
        };
    }
}