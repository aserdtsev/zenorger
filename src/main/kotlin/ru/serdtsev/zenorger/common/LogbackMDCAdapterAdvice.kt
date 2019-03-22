package ru.serdtsev.zenorger.common

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Component
@Aspect
class LogbackMDCAdapterAdvice {
    @Pointcut("execution(public String ch.qos.logback.classic.util.LogbackMDCAdapter.get(String))")
    fun getExecution(key: String?) {}

    @Around("getExecution(String)")
    fun getAdvice(pjp: ProceedingJoinPoint, key: String?): Any {
        val retVal = pjp.proceed()
        return retVal
    }
}