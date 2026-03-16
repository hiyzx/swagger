package org.zero.swagger.doc.web;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.swagger.doc.assembly.*;
import org.zero.swagger.doc.config.SwaggerDocCache;
import org.zero.swagger.doc.config.SwaggerDocConfig;
import org.zero.swagger.doc.enums.DocTypeEnum;
import org.zero.swagger.doc.reader.Reader;

import jakarta.annotation.Resource;

import java.util.Set;

/**
 * 提供 xxl/kafka/feign/custom 等扩展的 Swagger 2.0 文档 JSON
 * @author 水寒
 * @since 2025-03-11
 * @description 使用 swagger-core 1.x，返回 Swagger 2.0 JSON
 */
@RestController
@RequestMapping("/swagger-ext")
@Api(tags = "文档接口")
public class SwaggerDocController {

    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    @Value("${springdoc.api-docs.enabled:false}")
    private boolean enabled;

    @Resource
    private XxlJobManager xxlJobManager;
    @Resource
    private KafkaConsumerManager kafkaConsumerManager;
    @Resource
    private CustomServiceManager customServiceManager;
    @Resource
    private FeignServiceManager feignServiceManager;
    @Resource
    private EventListenerManager eventListenerManager;
    @Resource
    private SwaggerDocConfig swaggerDocConfig;
    @Resource
    private SwaggerDocCache swaggerDocCache;

    @GetMapping(value = "/xxl/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取xxlJob swagger数据")
    public ResponseEntity<String> getXxlApiList() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getXxlSwagger(), xxlJobManager.getClasses(), DocTypeEnum.XXL);
    }

    @GetMapping(value = "/kafka/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取kafka swagger数据")
    public ResponseEntity<String> getKafkaApiList() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getKafkaSwagger(), kafkaConsumerManager.getClasses(), DocTypeEnum.KAFKA);
    }

    @GetMapping(value = "/feign/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取feign swagger数据")
    public ResponseEntity<String> getFeignSwagger() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getFeignSwagger(), feignServiceManager.getClasses(), DocTypeEnum.FEIGN);
    }

    @GetMapping(value = "/custom/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取custom swagger数据")
    public ResponseEntity<String> getCustomSwagger() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getCustomSwagger(), customServiceManager.getClasses(), DocTypeEnum.CUSTOM);
    }

    @GetMapping(value = "/custom-token/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取custom-token swagger数据")
    public ResponseEntity<String> getCustomTokenSwagger() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getCustomTokenSwagger(), customServiceManager.getClasses(), DocTypeEnum.CUSTOM_TOKEN);
    }

    @GetMapping(value = "/event-listener/resources", produces = {MediaType.APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE})
    @ApiOperation("获取listener swagger数据")
    public ResponseEntity<String> getEventListenerSwagger() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getEventListenerSwagger(), eventListenerManager.getClasses(), DocTypeEnum.EVENT_LISTENER);
    }

    private ResponseEntity<String> getApiList(Swagger swagger, Set<Class<?>> classes, DocTypeEnum type) throws JsonProcessingException {
        if (!enabled) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (swagger != null) {
            return new ResponseEntity<>(Json.mapper().writeValueAsString(swagger), HttpStatus.OK);
        }
        swagger = new Swagger();
        buildSwagger(swagger, classes, type);
        return new ResponseEntity<>(Json.mapper().writeValueAsString(swagger), HttpStatus.OK);
    }

    private void buildSwagger(Swagger swagger, Set<Class<?>> classes, DocTypeEnum type) {
        if (swaggerDocConfig != null) {
            swaggerDocConfig.configure(swagger);
        }
        if (CollectionUtil.isNotEmpty(classes)) {
            String basePath = type.isToken() ? "/swagger-ext-token" + type.getPrefix() : "/swagger-ext" + type.getPrefix();
            Reader.read(swagger, classes, basePath);
        }
        if (type == DocTypeEnum.XXL) {
            swaggerDocCache.setXxlSwagger(swagger);
        } else if (type == DocTypeEnum.KAFKA) {
            swaggerDocCache.setKafkaSwagger(swagger);
        } else if (type == DocTypeEnum.FEIGN) {
            swaggerDocCache.setFeignSwagger(swagger);
        } else if (type == DocTypeEnum.CUSTOM) {
            swaggerDocCache.setCustomSwagger(swagger);
        } else if (type == DocTypeEnum.CUSTOM_TOKEN) {
            swaggerDocCache.setCustomTokenSwagger(swagger);
        } else if (type == DocTypeEnum.EVENT_LISTENER) {
            swaggerDocCache.setEventListenerSwagger(swagger);
        }
    }
}
