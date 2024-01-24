package com.huii.common.core.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页参数实体类
 *
 * @author huii
 */
@Data
public class PageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页大小
     */
    private Integer size;

    /**
     * 当前页数
     */
    private Integer current;

    /**
     * 偏移 用于手动计算分页参数
     */
    private Integer offset;

    /**
     * 总记录
     */
    private Integer total;

    public static long calcOffset(long current, long size) {
        long offset;
        if (current == 1) {
            offset = 0;
        } else {
            offset = (current - 1) * size;
        }
        return offset;
    }
}
