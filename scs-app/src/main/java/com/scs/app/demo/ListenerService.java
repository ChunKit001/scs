package com.scs.app.demo;

import com.scs.app.listener.event.DemoEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ListenerService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void demo2() throws RuntimeException {
        DemoEvent demoEvent = new DemoEvent();
        applicationEventPublisher.publishEvent(demoEvent);
    }
}
