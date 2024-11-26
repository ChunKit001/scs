package com.scs.listener.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class DemoEvent extends ApplicationEvent {
    public DemoEvent() {
        super("");
    }

    public DemoEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
