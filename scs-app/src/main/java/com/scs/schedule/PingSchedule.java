package com.scs.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PingSchedule {
    @Scheduled(fixedRate = 3600000)
    public void ping() {
        log.info("Pinging server");
    }
}
