package org.zero.swagger.demo.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author 水寒
 * @description 事件监听测试类
 * @since 2026/3/17
 */
@Component
public class MyEventListener {

    @EventListener
    public void handleCustomEvent(MyEvent event) {
        System.out.println("Custom Event Triggered: " + event.getId());
    }
}