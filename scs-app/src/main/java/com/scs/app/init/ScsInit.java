package com.scs.app.init;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScsInit implements InitializingBean, ApplicationRunner, CommandLineRunner {
    //启动执行
    @PostConstruct
    public void init() {
        log.info("PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("afterPropertiesSet");
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("ApplicationRunner run");
    }

    @Override
    public void run(String... args) {
        log.info("CommandLineRunner run");
    }
}
