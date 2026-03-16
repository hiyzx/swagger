package org.zero.swagger.doc.assembly;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author 水寒
 * @description 加载kafka消费者
 * @since 2025/3/11
 */
@Component
public class KafkaConsumerManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        super.init(Collections.singletonList(KafkaListener.class));
    }
}
