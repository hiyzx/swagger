package org.zero.swagger.doc.assembly;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yzx
 * @since  2024/7/16
 */
@Component
@Data
public abstract class AbstractBeanManager {

    protected Set<Class<?>> classes = null;
    protected Map<String, Method> methodMap = null;
    protected Map<String, Object> objectMap = null;

    public abstract void initClass();

    public Set<Class<?>> getClasses() {
        if (classes != null) {
            return classes;
        }
        initClass();
        return classes;
    }

    public Map<String, Method> getMethodMap() {
        if (methodMap != null) {
            return methodMap;
        }
        initClass();
        return methodMap;
    }

    public Map<String, Object> getObjectMap() {
        if (objectMap != null) {
            return objectMap;
        }
        initClass();
        return objectMap;
    }


    public void init(List<Class<? extends Annotation>> annotationClasses) {
        classes = new HashSet<>();
        methodMap = new HashMap<>();
        objectMap = new HashMap<>();
        // 找出所有的bean中带有注解 annotationClasses
        Map<String, Object> beans = SpringUtil.getApplicationContext().getBeansWithAnnotation(Component.class);
        for (Object bean : beans.values()) {
            Class<?> ultimateTargetClass = AopProxyUtils.ultimateTargetClass(bean);
            for (Method method : ultimateTargetClass.getDeclaredMethods()) {
                if (isAnnotationPresent(method, annotationClasses)) { // 指定需要查找的注解类
                    String methodName = String.format("%s/%s", ultimateTargetClass.getName(), method.getName());
                    methodMap.put(methodName, method);
                    objectMap.put(ultimateTargetClass.getName(), bean);
                    classes.add(ultimateTargetClass);
                }
            }
        }
    }

    private boolean isAnnotationPresent(Method method, List<Class<? extends Annotation>> annotationClasses) {
        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            if (method.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}