package com.scs.schedule;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PingSchedule implements InitializingBean, ApplicationRunner, CommandLineRunner {
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

    // 配置文件5min执行一次 缺失配置时候10min执行一次
    @Scheduled(cron = "${corn.ping:0 0/10 * * * *}")
    public void ping() {
        log.info("Pinging server");
    }


}