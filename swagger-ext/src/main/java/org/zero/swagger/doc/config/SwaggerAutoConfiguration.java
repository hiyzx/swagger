package org.zero.swagger.doc.config;

import io.swagger.config.SwaggerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Swagger 扩展自动配置（Spring Boot 3 通过 AutoConfiguration.imports 加载）
 * @author 水寒
 * @since  2025/3/11
 */
@AutoConfiguration
@ComponentScan(basePackages = {"org.zero.swagger.doc"})
public class SwaggerAutoConfiguration {

    @Bean
    public SwaggerConfig swaggerDocConfig() {
        return new SwaggerDocConfig();
    }

    @Bean
    public SwaggerDocCache swaggerDocCache() {
        return new SwaggerDocCache();
    }

}
