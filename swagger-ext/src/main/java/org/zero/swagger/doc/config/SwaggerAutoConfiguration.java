package org.zero.swagger.doc.config;

import io.swagger.config.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.zero.swagger.doc.kafka.KafkaConsumerScanner;
import org.zero.swagger.doc.xxl.XxlJobScanner;

@Configuration
@ComponentScan(basePackages = {"org.zero.swagger.doc"})
public class SwaggerAutoConfiguration {

    @Bean
    public SwaggerConfig swaggerDocConfig() {
        return new SwaggerDocConfig();
    }

    @Bean
    public XxlJobScanner xxlJobScanner() {
        return new XxlJobScanner();
    }

    @Bean
    public KafkaConsumerScanner kafkaConsumerScanner() {
        return new KafkaConsumerScanner();
    }

    @Bean
    public SwaggerDocCache swaggerDocCache() {
        return new SwaggerDocCache();
    }

}
