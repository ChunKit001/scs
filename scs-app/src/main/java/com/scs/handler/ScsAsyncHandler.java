package com.scs.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScsAsyncHandler {
    @Async
    public void scsAsyncHandler(){
        log.info("scsAsyncHandler");
    }
}
