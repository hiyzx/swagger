package org.zero.swagger.doc.assembly;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author 水寒
 * @description 加载xxl-job
 * @since 2025/3/11
 */
@Component
public class XxlJobManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        super.init(Collections.singletonList(XxlJob.class));
    }
}
