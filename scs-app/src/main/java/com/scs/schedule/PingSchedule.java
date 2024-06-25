package com.scs.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PingSchedule {
    @Scheduled(fixedRate = 3600000)
    public void ping() {
        System.out.println("Ping!");
    }
}
