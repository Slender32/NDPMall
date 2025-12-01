package com.slender.aspect;

import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.config.manager.JsonParserManager;
import com.slender.constant.other.RedisTime;
import com.slender.utils.CacheToolkit;
import com.slender.utils.StringToolkit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {
    private final StringRedisTemplate redisTemplate;
    private final JsonParserManager jsonParser;

    @Around("@annotation(annotation)")
    public Object cacheAround(ProceedingJoinPoint joinPoint,@Nullable ServiceCache annotation) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        Class<?> type = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        String key = CacheToolkit.getKey(type, id);

        String cached = redisTemplate.opsForValue().get(key);
        log.info("获取缓存:{}",key);
        if (!StringToolkit.isBlank(cached)) return jsonParser.parse(cached,type);

        Object result = joinPoint.proceed();

        if (result != null) {
            String value = jsonParser.format(result);
            redisTemplate.opsForValue().set(key, value,RedisTime.Entity.EXPIRE_TIME);
        }else redisTemplate.opsForValue().set(key,"", RedisTime.Entity.EXPIRE_TIME);
        return result;
    }

    @Before("@annotation(annotation)")
    public void before(JoinPoint joinPoint, @Nullable RemoveCache annotation){
        Long id = (Long) joinPoint.getArgs()[0];
        Class<?> clazz = joinPoint.getTarget().getClass();
        String key = CacheToolkit.getRemoveKey(clazz,id);
        redisTemplate.delete(key);
        log.info("删除缓存:{}",key);
    }

}
