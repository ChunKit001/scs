package com.scs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ScsAspect {

    /**
     * 10、使用excution表达式
     * execution(
     *  modifier-pattern?           //用于匹配public、private等访问修饰符
     *  ret-type-pattern            //用于匹配返回值类型，不可省略
     *  declaring-type-pattern?     //用于匹配包类型
     *  name-pattern(param-pattern) //用于匹配类中的方法，不可省略
     *  throws-pattern?             //用于匹配抛出异常的方法
     * )
     *
     * 下面的表达式解释为：匹配com.leo.controller.HelloController类中以hello开头的修饰符为public返回类型任意的方法
     */
    @Pointcut("execution (* com.scs.web.*.*(..))")
    public void pointCut1() {

    }

    @Before(value = "pointCut1()")
    public void beforeLog(JoinPoint joinPoint) {
        log.info("进入LogAop的beforeLogger");
    }


    @Around(value = "pointCut1()")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
        log.info("进入LogAop的aroundLogger");
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("return LogAop的aroundLogger");
        return proceed;
    }

    @After(value = "pointCut1()")
    public void afterLog() {
        log.info("进入LogAop的afterLogger");
    }

    @AfterReturning(value = "pointCut1()", returning = "o")
    public void afterRunningLog(Object o) {
        log.info("进入LogAop的afterRunningLog");
    }

    @AfterThrowing(value = "pointCut1()")
    public void afterThrowingLog() {
        log.info("进入LogAop的afterThrowingLog");
    }


    /**
     * 1、使用within表达式匹配
     * 下面示例表示匹配com.leo.controller包下所有的类的方法
     */
    @Pointcut("within(com.scs.web.*.*)")
    public void pointcutWithin(){

    }

    /**
     * 2、this匹配目标指定的方法，此处就是HelloController的方法
     */
    @Pointcut("this(com.scs.web)")
    public void pointcutThis(){

    }

    /**
     * 3、target匹配实现UserInfoService接口的目标对象
     */
    @Pointcut("target(com.scs.web)")
    public void pointcutTarge(){

    }

    /**
     * 4、bean匹配所有以Service结尾的bean里面的方法，
     * 注意：使用自动注入的时候默认实现类首字母小写为bean的id
     */
    @Pointcut("bean(*ServiceImpl)")
    public void pointcutBean(){

    }

    /**
     * 5、args匹配第一个入参是String类型的方法
     */
    @Pointcut("args(String, ..)")
    public void pointcutArgs(){

    }

    /**
     * 6、@annotation匹配是@Controller类型的方法
     */
    @Pointcut("@annotation(org.springframework.stereotype.Controller)")
    public void pointcutAnnocation(){

    }
    /**
     * 7、@within匹配@Controller注解下的方法，要求注解的@Controller级别为@Retention(RetentionPolicy.CLASS)
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void pointcutWithinAnno(){

    }
    /**
     * 8、@target匹配的是@Controller的类下面的方法，要求注解的@Controller级别为@Retention(RetentionPolicy.RUNTIME)
     */
    @Pointcut("@target(org.springframework.stereotype.Controller)")
    public void pointcutTargetAnno(){

    }
    /**
     * 9、@args匹配参数中标注为@Sevice的注解的方法
     */
    @Pointcut("@args(org.springframework.stereotype.Service)")
    public void pointcutArgsAnno(){

    }
}