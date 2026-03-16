package org.zero.swagger.doc.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;

/**
 * Swagger 2.0 文档基础路径配置
 * author：水寒
 * since：2025-03-11
 * description：实现 SwaggerConfig，为 Swagger 设置 basePath
 */
public class SwaggerDocConfig implements SwaggerConfig {

    @Resource
    private ServletContext servletContext;

    @Override
    public Swagger configure(Swagger swagger) {
        setBasePath(swagger);
        return swagger;
    }

    private void setBasePath(Swagger swagger) {
        if (StringUtils.isEmpty(swagger.getBasePath())) {
            String cp = servletContext.getContextPath();
            swagger.setBasePath(StringUtils.isEmpty(cp) ? "/" : cp);
        }
    }

    @Override
    public String getFilterClass() {
        return null;
    }
}
