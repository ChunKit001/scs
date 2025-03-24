package com.scs.start;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class TestVirtualThreadPool {
    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;


    @Before
    public void setUp() {

    }

    @Test
    public void testCustomerAddSuccess() {
        asyncTaskExecutor.execute(() -> {
            int a = 1 / 0;
            System.out.println(a);
        });
    }

    @After
    public void release() {

    }
}
