package com.huii.common.constants;

/**
 * @author huii
 */
public interface JobConstants {

    /**
     * 默认
     */
    int MISFIRE_DEFAULT = 0;

    /**
     * 立即触发执行
     */
    int MISFIRE_IGNORE_MISFIRES = 1;

    /**
     * 触发一次执行
     */
    int MISFIRE_FIRE_AND_PROCEED = 2;

    /**
     * 不触发立即执行
     */
    int MISFIRE_DO_NOTHING = 3;

    /**
     * 任务正常
     */
    int JOB_STATUS_OK = 0;

    /**
     * 任务暂停
     */
    int JOB_STATUS_PAUSE = 1;

    /**
     * 任务执行目标KEY
     */
    String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 任务名称
     */
    String TASK_CLASS_NAME = "TASK_CLASS_NAME";
}
