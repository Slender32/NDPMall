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
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    /**
     * 该切面方法第一个参数为ID,返回值为实体类(Entity下的类),
     * 用于将返回值缓存
     */
    @Around("@annotation(annotation)")
    public Object cacheAround(ProceedingJoinPoint joinPoint,@Nullable ServiceCache annotation) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        Class<?> type = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        String key = CacheToolkit.getKey(type, id);

        String cached = redisTemplate.opsForValue().get(key);
        if (!StringToolkit.isBlank(cached)) return jsonParser.parse(cached,type);

        Object result = joinPoint.proceed();

        if (result != null) {
            String value = jsonParser.format(result);
            redisTemplate.opsForValue().set(key, value,RedisTime.Entity.EXPIRE_TIME);
        }else redisTemplate.opsForValue().set(key,"", RedisTime.Entity.EXPIRE_TIME);
        return result;
    }

    /**
     * 该切面方法第一个参数为ID,只能用于ServiceImpl类,用来将缓存删除
     */
    @AfterReturning("@annotation(annotation)")
    public void before(JoinPoint joinPoint, @Nullable RemoveCache annotation){
        Long id = (Long) joinPoint.getArgs()[0];
        Class<?> clazz = joinPoint.getTarget().getClass();
        String key = CacheToolkit.getRemoveKey(clazz,id);
        redisTemplate.delete(key);
    }

}
