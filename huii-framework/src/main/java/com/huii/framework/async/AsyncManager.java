package com.huii.framework.async;

import com.huii.common.utils.SpringUtils;
import com.huii.common.utils.ThreadPoolUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author huii
 */
public class AsyncManager {
    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    private static final AsyncManager manager = new AsyncManager();

    private AsyncManager() {
    }

    public static AsyncManager manager() {
        return manager;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        int operateDelayTime = 10;
        executor.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadPoolUtils.shutdownAndAwaitTermination(executor);
    }
}
