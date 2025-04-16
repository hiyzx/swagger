package org.zero.swagger.doc.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2 // 标记项目启用 Swagger API 接口文档
@ConditionalOnExpression("${swagger.enabled:false}")
public class SwaggerExtConfiguration {

    /**
     * xxl接口
     */
    @Bean
    @Primary
    public SwaggerResourcesProvider newSwaggerResourcesProvider(Environment env, DocumentationCache documentationCache
            , DocumentationPluginsManager documentationPluginsManager) {
        boolean hasXxl = StrUtil.isNotEmpty(env.getProperty("xxl.job.addresses")) || StrUtil.isNotEmpty("xxl.job.admin.addresses");
        boolean hasKafka = StrUtil.isNotEmpty("spring.kafka.bootstrap-servers");
        return new InMemorySwaggerResourcesProvider(env, documentationCache, documentationPluginsManager) {

            @Override
            public List<SwaggerResource> get() {
                List<SwaggerResource> resources = super.get();
                if (hasXxl) {
                    SwaggerResource xxlSwaggerResource = new SwaggerResource();
                    xxlSwaggerResource.setName("xxl-job");
                    xxlSwaggerResource.setSwaggerVersion("2.0");
                    xxlSwaggerResource.setUrl("/swagger-ext/xxl/resources");
                    xxlSwaggerResource.setLocation("/swagger-ext/xxl/resources"); // 即将废弃，和 url 属性等价。
                    resources.add(resources.size(), xxlSwaggerResource);
                }
                if (hasKafka) {
                    SwaggerResource kafkaSwaggerResource = new SwaggerResource();
                    kafkaSwaggerResource.setName("kafka");
                    kafkaSwaggerResource.setSwaggerVersion("2.0");
                    kafkaSwaggerResource.setUrl("/swagger-ext/kafka/resources");
                    kafkaSwaggerResource.setLocation("/swagger-ext/kafka/resources"); // 即将废弃，和 url 属性等价。
                    resources.add(resources.size(), kafkaSwaggerResource);
                }

                SwaggerResource feignSwaggerResource = new SwaggerResource();
                feignSwaggerResource.setName("feign");
                feignSwaggerResource.setSwaggerVersion("2.0");
                feignSwaggerResource.setUrl("/swagger-ext/feign/resources");
                feignSwaggerResource.setLocation("/swagger-ext/feign/resources"); // 即将废弃，和 url 属性等价。
                resources.add(resources.size(), feignSwaggerResource);

                SwaggerResource customSwaggerResource = new SwaggerResource();
                customSwaggerResource.setName("custom");
                customSwaggerResource.setSwaggerVersion("2.0");
                customSwaggerResource.setUrl("/swagger-ext/custom/resources");
                customSwaggerResource.setLocation("/swagger-ext/custom/resources"); // 即将废弃，和 url 属性等价。
                resources.add(resources.size(), customSwaggerResource);

                SwaggerResource customTokenSwaggerResource = new SwaggerResource();
                customTokenSwaggerResource.setName("custom-token");
                customTokenSwaggerResource.setSwaggerVersion("2.0");
                customTokenSwaggerResource.setUrl("/swagger-ext/custom-token/resources");
                customTokenSwaggerResource.setLocation("/swagger-ext/custom-token/resources"); // 即将废弃，和 url 属性等价。
                resources.add(resources.size(), customTokenSwaggerResource);

                SwaggerResource eventListenerSwaggerResource = new SwaggerResource();
                eventListenerSwaggerResource.setName("eventListener");
                eventListenerSwaggerResource.setSwaggerVersion("2.0");
                eventListenerSwaggerResource.setUrl("/swagger-ext/event-listener/resources");
                eventListenerSwaggerResource.setLocation("/swagger-ext/event-listener/resources"); // 即将废弃，和 url 属性等价。
                resources.add(resources.size(), eventListenerSwaggerResource);
                return resources;
            }

        };
    }
}