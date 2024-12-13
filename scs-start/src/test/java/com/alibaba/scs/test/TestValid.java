package com.alibaba.scs.test;

import com.scs.Application;
import com.scs.api.DemoServiceI;
import com.scs.dto.ValidDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestValid {
    @Autowired
    private DemoServiceI demoServiceI;

    @Test
    public void testValid() {
        String s = demoServiceI.valid1(new ValidDTO());
        log.info("finish");
    }
}
