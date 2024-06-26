package com.scs.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * 配置@Async所使用的线程池
 *
 * @author : ChunKit.Cui
 * @date : 2020-07-25 10:27
 **/
@Slf4j
@Component
public class ThreadPoolConfig implements AsyncConfigurer {

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

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