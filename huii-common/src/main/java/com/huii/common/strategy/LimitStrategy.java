package com.huii.common.strategy;

public enum LimitStrategy {

    /**
     * 默认限流策略，基于redis，可实现对点限流
     */
    DEFAULT,

    /**
     * 令牌桶限流策略，基于guava，只可用于单机系统
     */
    BUCKET,

    /**
     * 滑动窗口限流策略
     */
    WINDOW
}
