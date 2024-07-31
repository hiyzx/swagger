package org.zero.swagger.doc.web;

import cn.hutool.http.HttpUtil;
import com.xxl.job.core.context.XxlJobContext;
import io.swagger.annotations.Api;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.swagger.doc.kafka.KafkaConsumerManager;
import org.zero.swagger.doc.xxl.XxlJobManager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("${swagger.doc.http:/swagger-ext}")
@Api(tags = "调度接口")
public class SwaggerExecuteController {

    @Resource
    private XxlJobManager xxlJobManager;
    @Resource
    private KafkaConsumerManager kafkaConsumerManager;

    @PostMapping(value = "/xxl/{interfaceClass}/{methodName}", produces = "application/json; charset=utf-8")
    public Object invokeJob(@PathVariable("interfaceClass") String interfaceClass,
                            @PathVariable("methodName") String methodName, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        Method method = xxlJobManager.getMethodMap().get(String.format("%s/%s", interfaceClass, methodName));
        Object object = xxlJobManager.getObjectMap().get(interfaceClass);
        // 设置参数
        setXxlJobContext(request);
        return method.invoke(object);
    }

    @PostMapping(value = "/kafka/{interfaceClass}/{methodName}", produces = "application/json; charset=utf-8")
    public Object invokeKafka(@PathVariable("interfaceClass") String interfaceClass,
                              @PathVariable("methodName") String methodName, HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        Method method = kafkaConsumerManager.getMethodMap().get(String.format("%s/%s", interfaceClass, methodName));
        Object object = kafkaConsumerManager.getObjectMap().get(interfaceClass);
        Object[] params = convertKafkaParam(request);
        return method.invoke(object, params);
    }

    private Object[] convertKafkaParam(HttpServletRequest request) throws IOException {
        String requestBody = HttpUtil.getString(request.getInputStream(), StandardCharsets.UTF_8, false);
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("", 0, 0L, "key", requestBody);
        Object[] objs = new Object[1];
        objs[0] = consumerRecord;
        return objs;
    }

    private void setXxlJobContext(HttpServletRequest request) throws IOException {
        String jobParam = HttpUtil.getString(request.getInputStream(), StandardCharsets.UTF_8, false);
        XxlJobContext xxlJobContext = new XxlJobContext(0L, jobParam, "", 0, 0);
        XxlJobContext.setXxlJobContext(xxlJobContext);
    }
}
