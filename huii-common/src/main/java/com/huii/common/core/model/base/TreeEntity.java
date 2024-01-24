package com.huii.common.core.model.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 树表基类
 *
 * @author huii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity<T> extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long parentId;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private Boolean childrenFlag;

    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
}