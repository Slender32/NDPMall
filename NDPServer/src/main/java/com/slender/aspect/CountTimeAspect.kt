package com.slender.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Order(10)
@Component
class CountTimeAspect {
    private val log = org.slf4j.LoggerFactory.getLogger(javaClass)
    @Around("execution(* com.slender.controller.*.*(..))")
    @Throws(Throwable::class)
    fun countTime(joinPoint: ProceedingJoinPoint): Any? {
        val name = joinPoint.signature.name
        log.info("进入{}方法", name)
        val start = System.currentTimeMillis()
        val result = joinPoint.proceed()
        val end = System.currentTimeMillis()
        log.info("{}方法耗时{}ms", name, end - start)
        return result
    }
}
