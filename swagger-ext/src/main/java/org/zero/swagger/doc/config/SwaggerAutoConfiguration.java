package org.zero.swagger.doc.config;

import io.swagger.config.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 叶招兴
 * @since  2025/3/11
 */
@Configuration
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
