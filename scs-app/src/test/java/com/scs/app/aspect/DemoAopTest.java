package com.scs.app.aspect;

import com.scs.app.demo.AspectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(DemoAopTest.AopConfig.class)
class DemoAopTest {

    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @Import({AspectService.class, DemoAop.class})
    static class AopConfig {
    }

    @Autowired
    private AspectService aspectService;

    @Autowired
    private DemoAop demoAop;

    @BeforeEach
    void reset() {
        demoAop.getAroundCount().set(0);
    }

    @Test
    void interceptsDemo2ButSkipsAnnotatedDemo1() {
        aspectService.demo1();
        assertThat(demoAop.getAroundCount().get()).isZero();

        aspectService.demo2();
        assertThat(demoAop.getAroundCount().get()).isEqualTo(1);
    }
}
