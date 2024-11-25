package com.scs.demo;

import com.scs.api.AspectServiceI;
import com.scs.aspect.anno.DemoAopAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AspectService implements AspectServiceI {
    @Override
    @DemoAopAnno
    public void demo1() throws RuntimeException {
        log.info("AspectService.demo1");
    }

    public void demo2() throws RuntimeException {
        log.info("AspectService.demo2");
    }
}
