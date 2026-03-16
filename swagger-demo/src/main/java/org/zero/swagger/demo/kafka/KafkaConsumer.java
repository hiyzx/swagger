package org.zero.swagger.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author 水寒
 * @since  2026/3/17
 * @description kafka消费者
 */
@Component
public class KafkaConsumer {


    @KafkaListener(topics = "test", groupId = "group1")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("Received message: " + record.value());
    }
}