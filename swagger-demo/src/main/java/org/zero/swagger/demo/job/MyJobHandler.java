package org.zero.swagger.demo.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author 水寒
 * @since  2026/3/17
 * @description xxl-job测试类
 */
@Component
public class MyJobHandler {

    @XxlJob("myJobHandler")
    public void execute() throws Exception {
        System.out.println("XXL-Job executed!");
    }
}