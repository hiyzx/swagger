package org.zero.swagger.doc.reader;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 叶招兴
 * @since 2024/9/19
 * @description 解析参数放到requestParam,不然拿到的是编译后的字段
 */
@Slf4j
public class RequestParameterNameDiscoverer implements ParameterNameDiscoverer {

    @Override
    @Nullable
    public String[] getParameterNames(Method method) {
        List<String> parameterNames = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                parameterNames.add(pathVariable.value());
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                parameterNames.add(requestParam.value());
            } else if (parameter.isAnnotationPresent(RequestHeader.class)) {
                RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
                parameterNames.add(requestHeader.value());
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                parameterNames.add("requestBody");
            } else if(parameter.isAnnotationPresent(SpringQueryMap.class)){
                parameterNames.add("springQueryMap");
            }else {
                parameterNames.add(parameter.getName());
            }

        }
        if (CollUtil.isEmpty(parameterNames)) {
            return null;
        }
        return parameterNames.toArray(new String[0]);
    }

    @Override
    @Nullable
    public String[] getParameterNames(Constructor<?> ctor) {
        return new String[0];
    }
}