package org.zero.swagger.doc.assembly;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Arrays;

/**
 * @author 叶招兴
 * @description
 * @since 2025/3/11
 */
@Component
public class EventListenerManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        super.init(Arrays.asList(EventListener.class, TransactionalEventListener.class));
    }
}
