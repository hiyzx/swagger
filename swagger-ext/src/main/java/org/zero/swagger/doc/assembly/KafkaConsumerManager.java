package org.zero.swagger.doc.assembly;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author 叶招兴
 * @description
 * @since 2025/3/11
 */
@Component
public class KafkaConsumerManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        super.init(Collections.singletonList(KafkaListener.class));
    }
}
