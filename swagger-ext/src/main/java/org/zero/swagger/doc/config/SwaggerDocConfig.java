package org.zero.swagger.doc.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

public class SwaggerDocConfig implements SwaggerConfig {

	@Resource
    private ServletContext servletContext;

	@Override
	public Swagger configure(Swagger swagger) {
		setBashPath(swagger);
		return swagger;
	}

	private void setBashPath(Swagger swagger) {
		if (StringUtils.isEmpty(swagger.getBasePath())){
			swagger.setBasePath(StringUtils.isEmpty(servletContext.getContextPath()) ? "/" : servletContext.getContextPath());
		}
	}

	@Override
	public String getFilterClass() {
		return null;
	}

}
