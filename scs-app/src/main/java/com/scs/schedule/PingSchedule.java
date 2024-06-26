package com.scs.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PingSchedule {
    //    配置文件5min执行一次 缺失配置时候10min执行一次
    @Scheduled(cron = "${corn.ping:0 0/10 * * * *}")
    public void ping() {
        log.info("Pinging server");
    }
}
