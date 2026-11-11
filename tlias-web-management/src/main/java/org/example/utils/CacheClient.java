package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
public class CacheClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 万能防穿透查询工具
     * @param cacheKey 缓存的key
     * @param returnType 返回的真实类型（用于反序列化）
     * @param dbFallback 查数据库的逻辑（Supplier 表示无参、有返回值的方法）
     */
    public <R> R queryWithPassThrough(String cacheKey, Class<R> returnType, Supplier<R> dbFallback) {
        // ① 查缓存
        Object cache = redisTemplate.opsForValue().get(cacheKey);

        if (cache != null) {
            if ("empty".equals(cache)) {
                log.info("命中空缓存，防穿透生效: {}", cacheKey);
                return null; // 或者根据业务返回空对象
            }
            log.info("命中缓存: {}", cacheKey);
            return (R) cache;
        }

        // ② 查数据库（这段逻辑由调用者传进来）
        log.info("未命中缓存，查数据库: {}", cacheKey);
        R result = dbFallback.get();

        // ③ 写回缓存（处理空值防穿透）
        if (result == null) {
            redisTemplate.opsForValue().set(cacheKey, "empty", 5, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);
        }

        return result;
    }
}