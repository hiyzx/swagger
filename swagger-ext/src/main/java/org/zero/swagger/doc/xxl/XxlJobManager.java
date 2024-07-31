package org.zero.swagger.doc.xxl;

import cn.hutool.extra.spring.SpringUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author yzx
 * @since  2024/7/16
 */
@Component
@Data
public class XxlJobManager {

    private Set<Class<?>> jobClasses = new HashSet<>();
    private Map<String, Method> methodMap = new HashMap<>();
    private Map<String, Object> objectMap = new HashMap<>();



    @PostConstruct
    public void init() {
        // 找出所有的bean中带有注解 xxljob
        Map<String, Object> beans = SpringUtil.getApplicationContext().getBeansWithAnnotation(Component.class);
        for (Object bean : beans.values()) {
            Class<?> ultimateTargetClass = AopProxyUtils.ultimateTargetClass(bean);
            for (Method method : ultimateTargetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(XxlJob.class)) { // 指定需要查找的注解类
                    String methodName = String.format("%s/%s", ultimateTargetClass.getName(), method.getName());
                    methodMap.put(methodName, method);
                    objectMap.put(ultimateTargetClass.getName(), bean);
                    jobClasses.add(ultimateTargetClass);
                }
            }
        }
    }
}
