package com.scs.start;

import com.scs.start.Application;
import com.scs.demo.AspectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestAop {
    @Autowired
    private AspectService aspectService;

    @Before
    public void setUp() {

    }

    @Test
    public void testCustomerAddSuccess() {
        aspectService.demo1();
        aspectService.demo2();
    }

    @After
    public void release() {

    }
}
