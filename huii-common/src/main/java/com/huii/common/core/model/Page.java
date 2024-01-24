package com.huii.common.core.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 *
 * @author huii
 */
@Data
public class Page implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 每页记录数
     */
    private int size;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页数
     */
    private int current;

    /**
     * 数据集合
     */
    private List<?> data;

    /**
     * 参数集合
     */
    private List<?> param;

    public Page(IPage<?> page) {
        this.data = page.getRecords();
        this.total = (int) page.getTotal();
        this.size = (int) page.getSize();
        this.current = (int) page.getCurrent();
        this.pages = (int) page.getPages();
    }
}