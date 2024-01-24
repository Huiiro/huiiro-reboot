package com.huii.framework.core.domain;

import lombok.Data;

/**
 * memory
 *
 * @author huii
 */
@Data
public class Mem {

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;
}
