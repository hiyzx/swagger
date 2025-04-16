package org.zero.swagger.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.swagger.demo.listener.MyEvent;

@RestController
public class EventController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/triggerEvent")
    public String triggerEvent() {
        eventPublisher.publishEvent(new MyEvent(1L));
        return "Event Triggered!";
    }
}