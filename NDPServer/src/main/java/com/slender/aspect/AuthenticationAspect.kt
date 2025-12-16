package com.slender.aspect

import com.slender.annotation.CheckAuthentication
import com.slender.exception.user.IllegalOperationException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class AuthenticationAspect {

    /**
     * 该切面方法第一个和第二个参数为ID,用于校验登录ID和操作ID是否一致
     */
    @Before("@annotation(annotation) || @within(annotation)")
    fun authenticate(joinPoint: JoinPoint, annotation: CheckAuthentication) {
        val args = joinPoint.args
        if (args[0] != args[1]) throw IllegalOperationException()
    }
}
