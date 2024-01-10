package com.huii.async.manager;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 关闭线程池管理器
 *
 * @author huii
 */
@Slf4j
@Component
public class ShutdownManager {

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager() {
        try {
            log.info("Thread-pool-close ...");
            AsyncManager.manager().shutdown();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
