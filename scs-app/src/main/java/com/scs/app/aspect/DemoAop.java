package com.scs.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 详细可参考
 * https://cloud.tencent.com/developer/article/2240290
 */
@Slf4j
@Aspect
@Component
public class DemoAop {
    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        log.info("DemoAspect.before");
    }

    /**
     * 表达式之间可以通过 && || !表示与或非
     * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?) 为方法匹配模式串
     * <p>
     * 以下为例
     *
     * @Pointcut("execution (* com.scs.demo.. * . * ( ..))")
     * execution(): 表达式主体。
     * 第一个*号：表示返回类型，*号表示所有的类型。
     * 包名：表示需要拦截的包名，一个句号表示当前包所有类  两个句点表示当前包与子孙包下所有类。
     * 第二个*号：表示类名，*号表示所有的类。
     * *(..):最后这个星号表示方法名，*号表示所有的方法，(..)表示任意参数，()为无参数，(String,int)接受String与int，(String,*)第一个入参为String第二个入参可以是任意类型不能有第三个参数，(String,..)为第一个String不定长，(Object+)表示为Object的子类
     * <p>
     * 类名的+号表示匹配AspectServiceI接口其子类所有实现类的方法
     * <p>
     * args(type-pattern1, type-pattern2, ...) 方法入参表达式
     * @Pointcut("args(java.lang.String)")
     * @args(annotation) 参数注解表达式 通过判别目标方法的运行时入参对象的类是否标注特定注解来指定连接点。
     * @Pointcut("@args(com.example.annotation.MyAnnotation)") 父类有注解，但子类没有注解的话，@within和@target是不会对子类生效的。
     * 子类没有注解的情况下，只有没有被重写的有注解的父类的方法才能被@within匹配到。
     * 如果父类无注解，子类有注解的话，@target对父类所有方法生效，@within只对重载过的方法生效。
     * this(type-pattern) 代理类按类型匹配于指定类，则被代理的目标类所有连接点匹配切点
     * @Pointcut("within(com.example.service.MyService)")
     * @Pointcut("target(com.example.service.MyService)")
     * @Pointcut("this(com.example.service.MyService)")
     * @within(annotation) 类注解表达式 子类也可使用
     * @target(annotation) 类注解表达式明确调用的方法 即不考虑实际类型（子类）
     * @Pointcut("@within(com.example.annotation.MyAnnotation)")
     * @Pointcut("@target(com.example.annotation.MyAnnotation)") this()用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口也类型匹配；
     */
    @Pointcut("execution (public void com.scs.demo.AspectService.*() throws java.lang.RuntimeException) && !@annotation(com.scs.aspect.anno.DemoAopAnno)")
//    @Pointcut("execution (public void com.scs.demo.AspectService.demo1())")
//    @Pointcut("execution (public void com.scs.demo.AspectService.demo*())")
//    @Pointcut("execution (public void com.scs.api.AspectServiceI+.*())")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("DemoAspect.around.start");
        Object proceed = null;
        try {
            proceed = pjp.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("DemoAspect.around.end");
        return proceed;
    }

    @After(value = "pointCut()")
    public void after() {
        log.info("DemoAspect.after");
    }

    @AfterReturning(value = "pointCut()", returning = "o")
    public void afterRunning(Object o) {
        log.info("DemoAspect.afterRunning");
    }

    @AfterThrowing(value = "pointCut()")
    public void afterThrowing() {
        log.info("DemoAspect.afterThrowing");
    }
}