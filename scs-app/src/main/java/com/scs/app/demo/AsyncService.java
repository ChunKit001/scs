package com.scs.app.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AsyncService {

    @Async
    public CompletableFuture<String> scsAsyncHandler() {
        Thread t = Thread.currentThread();
        log.info("scsAsyncHandler name={} virtual={}", t.getName(), t.isVirtual());
        return CompletableFuture.completedFuture(
                t.getName() + "|virtual=" + t.isVirtual());
    }
}
