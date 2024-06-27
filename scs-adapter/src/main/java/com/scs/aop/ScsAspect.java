package com.scs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ScsAspect {

    @Pointcut("execution (* com.scs.web.*.*(..))")
    public void test() {

    }

    @Before("test()")
    public void beforeAdvice() {
        log.info("before advice");
    }

    @After("test()")
    public void afterAdvice() {
        log.info("after advice");
    }

    @Around("test()")
    public void aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("before around");
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        log.info("after around");
    }
}