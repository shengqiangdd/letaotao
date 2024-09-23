package com.gxcy.letaotao.common.config.redis;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 自定义缓存key生成策略
 */
@Component
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append(":"); // 添加方法名

        for (Object param : params) {
            if (param == null) {
                continue; // 跳过 null 参数
            } else if (param instanceof List<?> list) {
                boolean firstItem = true;
                for (Object item : list) {
                    if (item == null) {
                        continue; // 跳过 null 元素
                    }
                    if (!firstItem) {
                        sb.append("-");
                    } else {
                        firstItem = false;
                    }
                    sb.append(item.toString());
                }
            } else {
                sb.append(param.toString()).append("_");
            }
        }

        // 移除最后一个多余的分隔符
        int length = sb.length();
        if (length > 0 && sb.charAt(length - 1) == '_') {
            sb.setLength(length - 1);
        }

        return sb.toString();
    }
}