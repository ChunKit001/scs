package com.alibaba.scs.test;

import com.scs.Application;
import com.scs.handler.ScsAsyncHandler;
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
public class TestAsync {

    @Autowired
    private ScsAsyncHandler scsAsyncHandler;

    @Before
    public void setUp() {

    }

    @Test
    public void testCustomerAddSuccess() {
        log.info("main thread");
        scsAsyncHandler.scsAsyncHandler();
        scsAsyncHandler.scsAsyncHandlerException();
    }

    @After
    public void release() {

    }
}
