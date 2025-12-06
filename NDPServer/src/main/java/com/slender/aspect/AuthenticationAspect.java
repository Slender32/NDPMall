package com.slender.aspect;

import com.slender.annotation.CheckAuthentication;
import com.slender.exception.user.IllegalOperationException;
import com.slender.exception.user.MerchantNotFoundException;
import com.slender.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {
    private final MerchantRepository merchantRepository;

    /**
     * 该切面方法第一个和第二个参数为ID,用于校验登录ID和操作ID是否一致
     */
    @Before("@annotation(annotation) || @within(annotation)")
    public void authenticate(JoinPoint joinPoint, CheckAuthentication annotation) {
        Object[] args = joinPoint.getArgs();
        if(!Objects.equals(args[0], args[1]))
            throw new IllegalOperationException();
    }

    @Before("execution(* com.slender.controller.MerchantController.*(..))")
    public void authenticate(JoinPoint joinPoint){
        Long uid =(Long) joinPoint.getArgs()[0];
        merchantRepository.getByUid(uid).orElseThrow(MerchantNotFoundException::new);
    }
}
