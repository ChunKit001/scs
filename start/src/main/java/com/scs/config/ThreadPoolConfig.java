package com.scs.config;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * 配置@Async所使用的线程池
 *
 * @author : ChunKit.Cui
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ThreadPoolConfig implements AsyncConfigurer {

    private final AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("a thread pool error has occurred ex={} "
                + "method={} params={}", ex, method, params);
    }
}