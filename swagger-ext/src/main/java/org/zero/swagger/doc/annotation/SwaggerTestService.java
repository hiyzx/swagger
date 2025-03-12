package org.zero.swagger.doc.annotation;

import java.lang.annotation.*;

/**
 * @author 叶招兴
 * @description
 * @since 2025/3/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface SwaggerTestService {
}
