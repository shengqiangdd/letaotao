package com.gxcy.letaotao.common.config.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 存缓存
    public void set(String key, String value, Long timeOut) {
        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
    }

    // 取缓存
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 缓存过期
    public boolean isExpire(String key) {
        return redisTemplate.hasKey(key) == null || Boolean.FALSE.equals(redisTemplate.hasKey(key));
    }

    // 清除缓存
    public void del(String key) {
        redisTemplate.delete(key);
    }
}
