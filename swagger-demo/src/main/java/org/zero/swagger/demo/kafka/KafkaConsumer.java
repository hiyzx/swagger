package org.zero.swagger.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {


    @KafkaListener(topics = "test", groupId = "group1")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("Received message: " + record.value());
    }
}