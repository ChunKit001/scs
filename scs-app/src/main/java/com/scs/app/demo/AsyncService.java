package com.scs.app.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncService {
    @Async
    public void scsAsyncHandler() {
        log.info("scsAsyncHandler");
        int a = 1 / 0;
    }

    public void scsAsyncHandlerException() {
        log.info("scsAsyncHandlerException");
    }
}
