package org.zero.swagger.demo.service;

import org.springframework.stereotype.Component;
import org.zero.swagger.doc.annotation.SwaggerTestService;

/**
 * @author 水寒
 * @description 自定义方法
 * @since 2025/4/16
 */
@Component
public class CustomService {

    @SwaggerTestService
    public String getId(Long id, String name) {
        return id + ":" + name;
    }
}
