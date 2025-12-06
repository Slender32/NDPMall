package com.slender.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Order(10)
@Component
public class CountTimeAspect {
    @Around("execution(* com.slender.controller.*.*(..))")
    public Object countTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final String name = joinPoint.getSignature().getName();
        log.info("进入{}方法", name);
        final long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        final long end = System.currentTimeMillis();
        log.info("{}方法耗时{}ms", name, end - start);
        return result;
    }
}
