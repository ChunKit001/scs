package com.scs.start;

import com.scs.start.Application;
import com.scs.demo.ListenerService;
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
public class TestEvent {

    @Autowired
    private ListenerService listenerService;

    @Before
    public void setUp() {

    }

    @Test
    public void testCustomerAddSuccess() throws InterruptedException {
        log.info("main thread");
        listenerService.demo2();
        Thread.sleep(10000L);
    }

    @After
    public void release() {

    }
}
