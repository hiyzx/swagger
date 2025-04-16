package org.zero.swagger.demo.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyEventListener {

    @EventListener
    public void handleCustomEvent(MyEvent event) {
        System.out.println("Custom Event Triggered: " + event.getId());
    }
}