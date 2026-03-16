package org.zero.swagger.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 水寒
 * @description feign测试类
 * @since 2026/3/17
 */
@FeignClient(value = "simple", url = "localhost:8080")
public interface SimpleFeign {

    @GetMapping("/hello")
    String hello(@RequestParam(value = "name") String name);
}
