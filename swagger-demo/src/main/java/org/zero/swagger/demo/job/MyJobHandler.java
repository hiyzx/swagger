package org.zero.swagger.demo.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class MyJobHandler {

    @XxlJob("myJobHandler")
    public void execute() throws Exception {
        System.out.println("XXL-Job executed!");
    }
}