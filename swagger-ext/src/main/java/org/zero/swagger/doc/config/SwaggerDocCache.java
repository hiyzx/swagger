package org.zero.swagger.doc.config;

import io.swagger.models.Swagger;
import lombok.Data;

/**
 * @author 叶招兴
 * @since  2025/3/11
 * @description
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
