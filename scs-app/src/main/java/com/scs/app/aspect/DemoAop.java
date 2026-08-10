package com.scs.app.aspect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AOP 演示：拦截 AspectService 中未标注 @DemoAopAnno 的方法。
 */
@Slf4j
@Aspect
@Component
public class DemoAop {

    @Getter
    private final AtomicInteger aroundCount = new AtomicInteger();

    @Pointcut("execution(public void com.scs.app.demo.AspectService.*()) "
            + "&& !@annotation(com.scs.app.aspect.anno.DemoAopAnno)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        aroundCount.incrementAndGet();
        log.info("DemoAspect.around.start method={}", pjp.getSignature().getName());
        try {
            return pjp.proceed();
        } finally {
            log.info("DemoAspect.around.end method={}", pjp.getSignature().getName());
        }
    }
}
