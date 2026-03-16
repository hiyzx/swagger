package org.zero.swagger.doc.web;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xxl.job.core.context.XxlJobContext;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.zero.swagger.doc.assembly.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 水寒
 * @since  2026/3/17
 * @description 调度接口
 */
@RestController
@RequestMapping({"/swagger-ext", "/swagger-ext-token"})
@Api(tags = "调度接口")
public class SwaggerExecuteController {

    @Resource
    private XxlJobManager xxlJobManager;
    @Resource
    private KafkaConsumerManager kafkaConsumerManager;
    @Resource
    private EventListenerManager eventListenerManager;
    @Resource
    private CustomServiceManager customServiceManager;
    @Resource
    private FeignServiceManager feignServiceManager;

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

    @PostMapping(value = "/feign/{interfaceClass}/{methodName}", produces = "application/json; charset=utf-8")
    public Object invokeFeign(@PathVariable("interfaceClass") String interfaceClass,
                              @PathVariable("methodName") String methodName, HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        Method method = feignServiceManager.getMethodMap().get(String.format("%s/%s", interfaceClass, methodName));
        Object object = feignServiceManager.getObjectMap().get(interfaceClass);
        Object[] params = convertFeignParam(request, method);
        return method.invoke(object, params);
    }

    @PostMapping(value = "/custom/{interfaceClass}/{methodName}", produces = "application/json; charset=utf-8")
    public Object invokeCustom(@PathVariable("interfaceClass") String interfaceClass,
                               @PathVariable("methodName") String methodName, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        Method method = customServiceManager.getMethodMap().get(String.format("%s/%s", interfaceClass, methodName));
        Object object = customServiceManager.getObjectMap().get(interfaceClass);
        Object[] params = convertCustomParam(request, method);
        return method.invoke(object, params);
    }

    @PostMapping(value = "/event-listener/{interfaceClass}/{methodName}", produces = "application/json; charset=utf-8")
    public Object invokeListener(@PathVariable("interfaceClass") String interfaceClass,
                                 @PathVariable("methodName") String methodName, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        Method method = eventListenerManager.getMethodMap().get(String.format("%s/%s", interfaceClass, methodName));
        Object object = eventListenerManager.getObjectMap().get(interfaceClass);
        Object[] params = convertCustomParam(request, method);
        return method.invoke(object, params);
    }

    private Object[] convertCustomParam(HttpServletRequest request, Method method) throws IOException {
        List<String> parameters = new ArrayList<>();
        if (CollUtil.isNotEmpty(request.getParameterMap())) {
            for (String key : request.getParameterMap().keySet()) {
                String[] strings = request.getParameterMap().get(key);
                if (strings.length > 0) {
                    parameters.add(strings[0]);
                }
            }
        }
        String requestBody = HttpUtil.getString(request.getInputStream(), StandardCharsets.UTF_8, false);
        Map<String, String> parameterMap = parseMapParameters(requestBody);
        parameters.addAll(parameterMap.values());
        Parameter[] methodParameters = method.getParameters();
        Type[] genericExceptionTypes = method.getGenericParameterTypes();
        Object[] args = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            Parameter param = methodParameters[i];
            args[i] = valueConvert2Obj(parameters.get(i), param.getType(), genericExceptionTypes[i]);
        }
        return args;
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
        XxlJobContext xxlJobContext = new XxlJobContext(0L, jobParam, "", 0, 1);
        XxlJobContext.setXxlJobContext(xxlJobContext);
    }

    private Object[] convertFeignParam(HttpServletRequest request, Method method) throws IOException {
        String requestBody = HttpUtil.getString(request.getInputStream(), StandardCharsets.UTF_8, false);
        Map<String, String> parameterMap = parseMapParameters(requestBody);
        if (CollUtil.isNotEmpty(request.getParameterMap())) {
            for (String key : request.getParameterMap().keySet()) {
                String[] strings = request.getParameterMap().get(key);
                if (strings.length > 0) {
                    parameterMap.put(key, strings[0]);
                }
            }
        }
        Parameter[] methodParameters = method.getParameters();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Object[] args = new Object[methodParameters.length];

        // 根据参数名称匹配值并进行转换
        for (int i = 0; i < methodParameters.length; i++) {
            Parameter param = methodParameters[i];
            // 从 Map 中获取对应参数名的值
            if (param.isAnnotationPresent(RequestParam.class)) {
                String value = parameterMap.get(param.getAnnotation(RequestParam.class).value());
                args[i] = valueConvert2Obj(value, param.getType(), genericParameterTypes[i]);
            } else if (param.isAnnotationPresent(PathVariable.class)) {
                String value = parameterMap.get(param.getAnnotation(PathVariable.class).value());
                args[i] = valueConvert2Obj(value, param.getType(), genericParameterTypes[i]);
            } else if (param.isAnnotationPresent(RequestHeader.class)) {
                String value = parameterMap.get(param.getAnnotation(RequestHeader.class).value());
                args[i] = valueConvert2Obj(value, param.getType(), genericParameterTypes[i]);
            } else if (param.isAnnotationPresent(RequestBody.class)) {
                String value = parameterMap.get("requestBody");
                args[i] = valueConvert2Obj(value, param.getType(), genericParameterTypes[i]);
            } else if (param.isAnnotationPresent(SpringQueryMap.class)) {
                String value = parameterMap.get("springQueryMap");
                args[i] = valueConvert2Obj(value, param.getType(), genericParameterTypes[i]);
            } else {
                // 不支持
            }
        }
        return args;
    }

    public Map<String, String> parseMapParameters(String input) {
        input = URLDecoder.decode(input, StandardCharsets.UTF_8);
        // 用于存储解析后的参数
        Map<String, String> rtnMap = new LinkedHashMap<>();
        if (StrUtil.isEmpty(input)) {
            return rtnMap;
        }

        // 判断输入是否是JSON格式
        if (input.trim().startsWith("{") || input.trim().startsWith("[")) {
            // 解析 JSON 字符串
            rtnMap.put("requestBody", input);
            rtnMap.put("springQueryMap", input);
            return rtnMap;
        } else if (input.contains("&") && input.contains("=")) {
            // 解析 URL 参数格式
            List<String> entry = StrUtil.split(input, "&");
            for (String s : entry) {
                rtnMap.put(StrUtil.split(s, "=").get(0), StrUtil.split(s, "=").get(1));
            }
            return rtnMap;
        } else if (input.contains("=")) {
            // 解析 URL 参数格式
            rtnMap.put(StrUtil.split(input, "=").get(0), StrUtil.split(input, "=").get(1));
            return rtnMap;
        } else {
            throw new IllegalArgumentException("无法识别的参数格式");
        }
    }

    public Object valueConvert2Obj(String value, Class<?> paramType, Type genericParameterType) {
        if (value.startsWith("{")) {
            return JSONUtil.toBean(value, paramType);
        } else if (value.startsWith("[")) {
            Type actualType = (genericParameterType instanceof ParameterizedType)
                    ? ((ParameterizedType) genericParameterType).getActualTypeArguments()[0] : Object.class;
            Class<?> actualClass = (actualType instanceof Class) ? (Class<?>) actualType : Object.class;
            List<?> list = JSONUtil.toList(value, actualClass);
            if (paramType.isAssignableFrom(Set.class)) {
                return new HashSet<>(list);
            } else {
                return list;
            }
        } else {
            // 检查参数类型是否匹配
            if (paramType.isInstance(value)) {
                return value;
            } else {
                // 如果类型不匹配，可以进行一些常见的类型转换
                if (paramType == int.class || paramType == Integer.class) {
                    return Integer.parseInt(value);
                } else if (paramType == double.class || paramType == Double.class) {
                    return Double.parseDouble(value);
                } else if (paramType == boolean.class || paramType == Boolean.class) {
                    return Boolean.parseBoolean(value);
                } else if (paramType == long.class || paramType == Long.class) {
                    return Long.parseLong(value);
                } else if (paramType == float.class || paramType == Float.class) {
                    return Float.parseFloat(value);
                } else if (paramType == short.class || paramType == Short.class) {
                    return Short.parseShort(value);
                } else if (paramType == byte.class || paramType == Byte.class) {
                    return Byte.parseByte(value);
                } else if (paramType == String.class) {
                    return value;
                }
            }
        }
        return null;
    }
}
