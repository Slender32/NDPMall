package com.slender.aspect;

import com.slender.annotation.CheckAuthentication;
import com.slender.exception.user.IllegalOperationException;
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

    @Before("@annotation(annotation)")
    public void authenticate(JoinPoint joinPoint, CheckAuthentication annotation) {
        Object[] args = joinPoint.getArgs();
        if(!Objects.equals(args[0], args[1]))
            throw new IllegalOperationException();
    }
}
