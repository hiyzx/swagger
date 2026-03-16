package org.zero.swagger.doc.config;

import io.swagger.models.Swagger;
import lombok.Data;

/**
 * @author 水寒
 * @description
 * @since 2025/3/11
 */
@Data
public class SwaggerDocCache {

    private Swagger xxlSwagger;

    private Swagger kafkaSwagger;

    private Swagger feignSwagger;

    private Swagger customSwagger;

    private Swagger customTokenSwagger;

    private Swagger eventListenerSwagger;
}
