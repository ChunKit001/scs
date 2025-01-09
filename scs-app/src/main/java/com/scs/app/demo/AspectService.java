package com.scs.app.demo;

import com.scs.app.aspect.anno.DemoAopAnno;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AspectService {
    @DemoAopAnno
    public void demo1() throws RuntimeException {
        log.info("AspectService.demo1");
    }

    public void demo2() throws RuntimeException {
        log.info("AspectService.demo2");
    }
}
