package org.zero.swagger.doc.assembly;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author 叶招兴
 * @description
 * @since 2025/3/11
 */
@Component
@Data
public class FeignServiceManager extends AbstractBeanManager {

    @Override
    public void initClass() {
        classes = new HashSet<>();
        methodMap = new HashMap<>();
        objectMap = new HashMap<>();
        // 找出所有的bean中带有注解 annotationClasses
        Map<String, Object> beans = SpringUtil.getApplicationContext().getBeansWithAnnotation(FeignClient.class);
        try {
            for (Map.Entry<String, Object> entry : beans.entrySet()) {
                String className = entry.getKey();
                Object bean = entry.getValue();
                Class<?> targetClass = Class.forName(className);
                for (Method method : targetClass.getDeclaredMethods()) {
                    String methodName = String.format("%s/%s", className, method.getName());
                    methodMap.put(methodName, method);
                    objectMap.put(className, bean);
                    classes.add(targetClass);
                }

            }
        } catch (Exception ex) {

        }
    }
}
