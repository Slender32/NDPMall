package com.slender.aspect

import com.slender.annotation.RemoveCache
import com.slender.annotation.ServiceCache
import com.slender.config.manager.JsonParserManager
import com.slender.constant.other.RedisTime
import com.slender.utils.CacheToolkit
import com.slender.utils.RandomToolkit
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Aspect
@Component
class CacheAspect(
    private val redisTemplate: StringRedisTemplate,
    private val jsonParser: JsonParserManager
) {
    companion object{
        const val CACHE_NULL_SYMBOL = "null"
    }

    /**
     * 该切面方法第一个参数为ID,返回值为实体类(Entity下的类),
     * 用于将返回值缓存
     */
    @Around("@annotation(annotation)")
    @Throws(Throwable::class)
    fun cacheAround(joinPoint: ProceedingJoinPoint, annotation: ServiceCache): Any? {
        val id = joinPoint.args[0] as Long
        val type = (joinPoint.signature as MethodSignature).returnType
        val key = CacheToolkit.getKey(type, id)

        val cached = redisTemplate.opsForValue().get(key)
        if (cached!=null && cached != CACHE_NULL_SYMBOL) return jsonParser.parse(cached, type)

        val result = joinPoint.proceed()

        val time=RedisTime.Entity.EXPIRE_TIME.plusMillis(RandomToolkit.randomExpireTime)
        if (result != null) {
            val value = jsonParser.format(result)
            redisTemplate.opsForValue().set(key, value,time )
        } else redisTemplate.opsForValue().set(key, CACHE_NULL_SYMBOL, time)
        return result
    }

    /**
     * 该切面方法第一个参数为ID,只能用于ServiceImpl类,用来将缓存删除
     */
    @AfterReturning("@annotation(annotation)")
    fun before(joinPoint: JoinPoint, annotation: RemoveCache) {
        val id = joinPoint.args[0] as Long
        val clazz: Class<*> = joinPoint.target.javaClass
        val key = CacheToolkit.getRemoveKey(clazz, id)
        redisTemplate.delete(key)
    }
}
