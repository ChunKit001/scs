package com.scs.app.listener;

import com.scs.app.listener.event.DemoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoListener {
    @Async
    @EventListener
    public void onApplicationEvent(DemoEvent event) {
        log.info("DemoListener.onApplicationEvent");
    }
}
