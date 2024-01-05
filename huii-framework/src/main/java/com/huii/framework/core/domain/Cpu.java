package com.huii.framework.core.domain;

import lombok.Data;

@Data
public class Cpu {

    /**
     * 核心数
     */
    private int cpuCore;

    /**
     * CPU型号
     */
    private String cpuType;

    /**
     * CPU总使用率
     */
    private double totalUsed;

    /**
     * CPU系统使用率
     */
    private double sysUsed;

    /**
     * CPU用户使用率
     */
    private double userUsed;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;
}
