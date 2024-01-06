package com.huii.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试任务
 *
 * @author huii
 */
@Slf4j
@Component("huiiTask")
public class DemoTask {

    public void demoTaskWithParams(String param) {
        log.info("执行有参任务：" + param);
    }

    public void demoTaskWithoutParams() {
        log.info("执行无参任务：");
    }
}
