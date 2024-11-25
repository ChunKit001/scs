package com.scs.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Component
public class ScsAsyncHandler {
    @Async
    public void scsAsyncHandler(){
        log.info("scsAsyncHandler");
    }

    public void scsAsyncHandlerException(){
        log.info("scsAsyncHandler");
        int a = 1/0;
    }
}
