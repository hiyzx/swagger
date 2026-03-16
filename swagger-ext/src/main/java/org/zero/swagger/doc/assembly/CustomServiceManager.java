package org.zero.swagger.doc.assembly;

import org.springframework.stereotype.Component;
import org.zero.swagger.doc.annotation.SwaggerTestService;

import java.util.Collections;

/**
 * @author 水寒
 * @description 加载自定义方法
 * @since 2025/3/11
 */
@Component
public class CustomServiceManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        super.init(Collections.singletonList(SwaggerTestService.class));
    }
}
