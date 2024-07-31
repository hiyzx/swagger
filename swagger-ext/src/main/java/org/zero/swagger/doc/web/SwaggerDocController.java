package org.zero.swagger.doc.web;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.swagger.doc.config.SwaggerDocCache;
import org.zero.swagger.doc.config.SwaggerDocConfig;
import org.zero.swagger.doc.enums.DocTypeEnum;
import org.zero.swagger.doc.kafka.KafkaConsumerScanner;
import org.zero.swagger.doc.reader.Reader;
import org.zero.swagger.doc.xxl.XxlJobScanner;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author yzx
 * @since 2024/4/26
 */
@RestController
@RequestMapping("/swagger-ext")
@Api(tags = "文档接口")
public class SwaggerDocController {

    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    @Value("${swagger.enabled}")
    private boolean enabled;

    @Resource
    private XxlJobScanner xxlJobScanner;
    @Resource
    private KafkaConsumerScanner kafkaConsumerScanner;
    @Resource
    private SwaggerDocConfig swaggerDocConfig;
    @Resource
    private SwaggerDocCache swaggerDocCache;


    @GetMapping(value = "/xxl/resources",
            produces = {"application/json; charset=utf-8", HAL_MEDIA_TYPE})
    @ApiOperation("获取xxlJob swagger数据")
    public ResponseEntity<Json> getXxlApiList() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getXxlSwagger(), xxlJobScanner.classes(), DocTypeEnum.XXL);
    }

    @GetMapping(value = "/kafka/resources",
            produces = {"application/json; charset=utf-8", HAL_MEDIA_TYPE})
    @ApiOperation("获取kafka swagger数据")
    public ResponseEntity<Json> getKafkaApiList() throws JsonProcessingException {
        return getApiList(swaggerDocCache.getKafkaSwagger(), kafkaConsumerScanner.classes(), DocTypeEnum.KAFKA);
    }

    private ResponseEntity<Json> getApiList(Swagger swagger, Set<Class<?>> classes, DocTypeEnum type) throws JsonProcessingException {
        if (!enabled) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (null != swagger) {
            return new ResponseEntity<>(new Json(io.swagger.util.Json.mapper().writeValueAsString(swagger)), HttpStatus.OK);
        } else {
            swagger = new Swagger();
        }
        swagger(swagger, classes, type);

        return new ResponseEntity<>(new Json(io.swagger.util.Json.mapper().writeValueAsString(swagger)), HttpStatus.OK);

    }

    private void swagger(Swagger swagger, Set<Class<?>> classes, DocTypeEnum type) {
        final SwaggerConfig configurator = swaggerDocConfig;
        if (configurator != null) {
            configurator.configure(swagger);
        }

        if (CollectionUtil.isNotEmpty(classes)) {
            // http请求地址，http://ip:port/swagger-ext/xxl/com.XXX.XxService/method
            Reader.read(swagger, classes, "/swagger-ext" + type.getPrefix());
        }
        if (type == DocTypeEnum.XXL) {
            swaggerDocCache.setXxlSwagger(swagger);
        } else if (type == DocTypeEnum.KAFKA) {
            swaggerDocCache.setKafkaSwagger(swagger);
        }
    }

}
