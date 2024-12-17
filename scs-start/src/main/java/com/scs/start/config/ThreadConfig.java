package com.scs.start.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
@Slf4j
public class ThreadConfig {

    @Bean(name = "asyncTaskExecutor")
    public AsyncTaskExecutor applicationTaskExecutor() {
        Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual();
        ThreadFactory factory = ofVirtual.name("vt-", 0L)
                .inheritInheritableThreadLocals(true)
                .uncaughtExceptionHandler((t, e) -> log.error("virtual thread pool uncaughtException,{}", t, e))
                .factory();
        ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);

        return new TaskExecutorAdapter(executorService);
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(applicationTaskExecutor());
    }
}