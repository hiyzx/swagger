package org.zero.swagger.doc.config;

import io.swagger.models.Swagger;
import lombok.Data;

@Data
public class SwaggerDocCache {

	private Swagger xxlSwagger;

	private Swagger kafkaSwagger;

	private Swagger feignSwagger;
}
