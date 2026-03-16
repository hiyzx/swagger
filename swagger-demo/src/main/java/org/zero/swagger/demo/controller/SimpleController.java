package org.zero.swagger.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 水寒
 * @since  2026/3/17
 * @description controller接口测试类
 */
@RestController
@Tag(name = "测试")
public class SimpleController {

    @GetMapping("/hello")
    @Operation(summary = "测试hello")
    public String hello(@RequestParam String name) {
        return "Hello, " + name + "!";
    }
}